<template>
  <div>
    <n-form-item>
      <n-input-group>
        <n-input v-model:value="tagName" placeholder="输入新标签名称" :style="{ width: '240px' }" />
        <n-button type="primary" ghost>
          新增标签
        </n-button>
      </n-input-group>
    </n-form-item>
    <n-data-table
        :columns="columns"
        :data="data"
        :pagination="pagination"
        :bordered="false"
    />
  </div>
</template>

<script lang="ts" setup>
import {definePageMeta, onMounted, reactive, ref} from "#imports";
import {DataTableColumns, NAvatar, NButton, NImage, NInput, PaginationInfo, PaginationProps} from "naive-ui";
import {$fetch} from "ohmyfetch";
import EditHallModal from "~/components/hall/EditHallModal.vue";
import {Ref} from "@vue/reactivity";

definePageMeta({
  name: "tag",
  layout: "admin"
})
const data = ref([] as Room[])
const tagName = ref('')
const pagination = reactive({
  page: 1,
  pageSize: 10,
} as PaginationProps)
const getList = async (page = 1, pageSize = 10) => {
  const res = await $fetch(`/api/room/tags`)
  data.value = res.data
  pagination.page = page
  pagination.itemCount = res.data.total
}
const editTag = async (name: string, id?: number) => {
  await $fetch('/api/room/editTag', {
    method: 'post',
    body: {
      name,
      id
    }
  })
  if (!id){
    tagName.value = ''
  }
  getList()
}
onMounted(() => getList())
const columns: DataTableColumns = [{
  title: "编号",
  key: "id"
}, {
  title: "标签名称",
  key: "name",
  render (row, index) {
    return h(NInput, {
      value: row.name,
      onUpdateValue (v) {
        // todo 防抖
        data.value[index].name = v
        editTag(v,row.id as number)
      }
    })
  }
},{
  title: "操作",
  key: "action",
  render(row){
    return h(NButton,{
      text:true,
      tag:'a',
      type:"primary",
      onClick:async () => {
        await $fetch('/api/room/tag?id=' + row.id, {
          method: 'delete'
        })
        getList()
      }
    },'删除')
  }
}]
</script>

<style scoped>

</style>
