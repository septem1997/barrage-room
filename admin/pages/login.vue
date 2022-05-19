<template>
  <div class="login-page">
    <n-card class="card">
      <n-form>
        <n-form-item label="账号">
          <n-input value="admin"/>
        </n-form-item>
        <n-form-item label="密码">
          <n-input type="password" value="admin"/>
        </n-form-item>
        <n-form-item>
          <n-button style="width: 100%" type="primary" @click="login">登陆</n-button>
        </n-form-item>
      </n-form>
    </n-card>
  </div>
</template>

<script lang="ts">
import {defineNuxtComponent, useRouter} from "#imports";

export default defineNuxtComponent({
  name: "login",
  layout: "blank",
  setup() {
    const router = useRouter()
    const login = () => {
      router.replace('/')
    }
    return {
      login: login
    }
  }
})
</script>

<style scoped lang="stylus">
.login-page {
  width 100vw
  height 100vh
  display flex
  align-items center
  justify-content center

  .card {
    width 360px
  }
}
</style>
