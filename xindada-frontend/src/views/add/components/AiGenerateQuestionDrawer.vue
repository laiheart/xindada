<template>
  <a-button type="outline" @click="handleClick">AI 生成题目</a-button>
  <a-drawer
    :width="340"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title>AI 生成题目</template>
    <div>
      <a-form
        :model="form"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
      >
        <a-form-item label="应用 id">
          {{ appId }}
        </a-form-item>
        <a-form-item field="questionNumber" label="题目数量">
          <a-input-number
            min="0"
            max="20"
            v-model="form.questionNumber"
            placeholder="请输入题目数量"
          />
        </a-form-item>
        <a-form-item field="optionNumber" label="选项数量">
          <a-input-number
            min="0"
            max="6"
            v-model="form.optionNumber"
            placeholder="请输入选项数量"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button
              :loading="submitting"
              type="primary"
              html-type="submit"
              style="width: 120px"
            >
              {{ submitting ? "生成中" : "一键生成" }}
            </a-button>
            <a-button
              :loading="submittingSse"
              type="primary"
              @click="handleSubmitSse"
              style="width: 120px"
            >
              {{ submittingSse ? "生成中" : "sse生成" }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults } from "vue";
import API from "@/api";
import { aiGenerateQuestionUsingPost } from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";

interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  }
});

const form = reactive({
  optionNumber: 2,
  questionNumber: 10
} as API.AiGenerateQuestionRequest);

const visible = ref(false);
const submitting = ref(false);
const submittingSse = ref(false);

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  const res = await aiGenerateQuestionUsingPost({
    appId: props.appId as any,
    ...form
  });
  if (res.data.code === 0 && res.data.data.length > 0) {
    if (props.onSuccess) {
      props.onSuccess(res.data.data);
    } else {
      message.success("生成题目成功");
    }
    // 关闭抽屉
    handleCancel();
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;
};
/**
 *  sse生成题目
 */
const handleSubmitSse = async () => {
  if (!props.appId) {
    return;
  }
  const data = [];
  submittingSse.value = true;
  const event = new EventSource("http://localhost:8101/api/question/ai_generate_sse?" +
    `appId=${props.appId}&questionNumber=${form.questionNumber}&optionNumber=${form.optionNumber}`);
  event.onopen = function(e) {
    console.log("onopen", e);
  };
  event.onmessage = function(e) {
    data.push(JSON.parse(e.data));
    console.log("onmessage", e.data);
  };
  event.onerror = function(e) {
    console.log("onerror", e);
  };
  event.addEventListener("close", function(e) {
    console.log("close", e);
    submittingSse.value = false;
    visible.value = false;
    console.log("data", data);
    props.onSuccess(data);
    event.close();
  });
};
</script>