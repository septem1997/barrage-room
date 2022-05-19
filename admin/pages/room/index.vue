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
import {DataTableColumns, NAvatar, NImage, PaginationProps} from "naive-ui";
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
  const res = await $fetch(`/api/room/list`, {
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
  key: "roomNo"
}, {
  title: "房间名称",
  key: "name"
}, {
  title: "房间创建人",
  key: "host.username"
}, {
  title: "创建时间",
  key: "createTime"
}]
</script>

<style scoped>

</style>
