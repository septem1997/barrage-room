<template>
  <n-config-provider
      :locale="zhCN"
      :date-locale="dateZhCN"
      :theme="darkTheme">
    <n-layout position="absolute">
      <n-layout-header style="height: 64px; padding: 24px" bordered>
        共享弹幕后台管理
      </n-layout-header>
      <n-layout has-sider position="absolute" style="top: 64px; bottom: 64px">
        <n-layout-sider bordered content-style="padding: 24px;">
          <n-menu @update:value="onKeyUpdate" :options="treeMenu" />
        </n-layout-sider>
        <n-layout content-style="padding: 24px;">
          <n-card>
            <slot/>
          </n-card>
        </n-layout>
      </n-layout>
      <n-layout-footer
          bordered
          position="absolute"
          style="height: 64px; padding: 24px"
      >
        Product by Septem1997
      </n-layout-footer>
    </n-layout>
  </n-config-provider>
</template>

<script lang="ts">
import {defineComponent, reactive, ref, useRouter} from "#imports";
import { darkTheme,zhCN, dateZhCN } from 'naive-ui'
export default defineComponent({
  name: "admin",
  components:{
  },
  setup() {
    const treeMenu: Array<TreeMenuItem> = reactive([
      {
        label: "大厅管理",
        key: "/hall/",
        children: [
          {
            label: "大厅列表",
            key: "/hall/"
          },
          {
            label: "标签列表",
            key: "/hall/tag"
          }
        ]
      }, {
        label: "房间管理",
        key: "/room/",
        children: [
          {
            label: "房间列表",
            key: "/room/"
          }
        ]
      }
    ])
    const router = useRouter()
    const onKeyUpdate = (key:string)=>{
      router.replace(key)
    }
    return {
      onKeyUpdate,
      treeMenu: treeMenu,
      darkTheme,
      zhCN,
      dateZhCN
    }
  }
})
</script>

<style scoped lang="stylus">
.container {
  width 1200px
  height 100vh
  margin 0 auto
  display flex
  background black

  .menu {
    width 240px
  }

  .content {
    flex 1
    width 0
    height 100%
  }
}
</style>
