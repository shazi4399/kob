<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">
      创建Bot
    </el-button>
    <el-dialog
        v-model="dialogVisible"
        title="新增Bot"
        width="70%"
        @close="closeDialog"
    >
      <el-form label-width="120px" label-position="top">
        <el-form-item label="Bot名称">
          <el-input type="text" v-model="botAdd.title" placeholder="请输入Bot名称"/>
        </el-form-item>
        <el-form-item label="Bot简介">
          <el-input
              v-model="botAdd.description"
              maxlength="300"
              placeholder="请输入Bot简介"
              show-word-limit
              type="textarea"
              rows="2"
          />
        </el-form-item>

        <p>Bot代码</p>
        <!--        <el-form-item label="Bot代码">-->
        <!--        </el-form-item>-->
        <VAceEditor
            v-model:value="botAdd.content"
            lang="c_cpp"
            :options="{
              fontSize: 16,
            }"
            theme="textmate"
            style="height: 250px"/>

        <p style="color:red" class="error-message">{{ botAdd.error_message }}</p>
      </el-form>
      <template #footer>
           <span class="dialog-footer">
              <el-button @click="add_bot" type="primary">
                确定
              </el-button>
              <el-button id="cancelButton" @click="dialogVisible = false">
                 取消
              </el-button>
          </span>
      </template>


    </el-dialog>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import $ from 'jquery';
import {useStore} from "vuex";
import {ElNotification} from "element-plus";

import {VAceEditor} from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
  name: "AddBot",
  components:{
    VAceEditor,
  },
  setup(props, context) {
    ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

    const dialogVisible = ref(false);
    const store = useStore();


    const botAdd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });

    const successMessage=()=>{
      ElNotification({
        title: '成功',
        message: '恭喜您，Bot创建成功。',
        type: 'success',
      })
    }

    const add_bot = () => {
      botAdd.error_message = "";
      $.ajax({
        url: ("https://app5275.acapp.acwing.com.cn/api/user/bot/add/"),
        type: "post",
        data: {
          title: botAdd.title,
          description: botAdd.description,
          content: botAdd.content,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            successMessage();
            context.emit('refresh_bots');
            $("#cancelButton").trigger('click');
          } else {
            botAdd.error_message = resp.error_message;
            // console.log("push2")
          }
        }
      })
    }

    const closeDialog = () => {
      botAdd.title = "";
      botAdd.description = "";
      botAdd.content = "";
      botAdd.error_message = "";
    }

    return {
      dialogVisible,
      botAdd,
      add_bot,
      closeDialog,
    }
  }
}
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>