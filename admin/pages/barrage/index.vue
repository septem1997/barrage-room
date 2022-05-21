<template>
  <n-data-table
      remote
      :columns="columns"
      :data="data"
      :pagination="pagination"
      @update:page="handlePageChange"
      :bordered="false"
  />
</template>

<script lang="ts" setup>
import {definePageMeta, onMounted, reactive, ref} from "#imports";
import {DataTableColumns, NAvatar, NEllipsis, NImage, PaginationProps} from "naive-ui";
import {$fetch} from "ohmyfetch";

definePageMeta({
  name: "index",
  layout: "admin"
})
const data = ref([] as Room[])
const pagination = reactive({
  page: 1,
  pageSize: 10,
} as PaginationProps)
const getList = async (page = 1, pageSize = 10) => {
  const res = await $fetch(`/api/barrage/all`, {
    params: {
      page,
      pageSize
    }
  })
  data.value = res.data.data
  pagination.page = page
  pagination.itemCount = res.data.total
}
const handlePageChange = (page)=>{
  getList(page)
}
onMounted(()=>getList())
const columns: DataTableColumns = [{
  title: "房间号",
  key: "room.roomNo"
}, {
  title: "房间名称",
  key: "room.name"
},{
  title: "弹幕内容",
  key: "content",
  render(row){
    return h(NEllipsis,{

    },row.content)
  }
}, {
  title: "弹幕发送人",
  key: "creator.nickname"
}, {
  title: "发送时间",
  key: "createTime"
}]
</script>

<style scoped>

</style>
