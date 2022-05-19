<template>
  <n-data-table
      :columns="columns"
      :data="data"
      :pagination="pagination"
      :bordered="false"
  />
</template>

<script lang="ts" setup>
import {definePageMeta, onMounted, reactive, ref} from "#imports";
import {DataTableColumns, NAvatar, NImage, PaginationInfo, PaginationProps} from "naive-ui";
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
  const res = await $fetch(`/api/room/hall/list`, {
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
  title: "编号",
  key: "roomNo"
}, {
  title: "大厅名称",
  key: "name"
}, {
  title: "图标",
  key: "roomIcon",
  render(row){
    return h(
        NImage,
        {
          style:{
            width:'40px',
            height:'40px'
          },
          src:row.roomIcon
        }
    )
  }
}, {
  title: "大厅标签",
  key: "tag.name"
}, {
  title: "创建时间",
  key: "createTime"
}]
</script>

<style scoped>

</style>
