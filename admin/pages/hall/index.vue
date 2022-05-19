<template>
  <div>
    <n-form-item>
      <n-button @click="addHall">新增大厅</n-button>
    </n-form-item>
    <n-data-table
        :columns="columns"
        :data="data"
        :pagination="pagination"
        :bordered="false"
    />
    <edit-hall-modal :id="editId" v-model:visible="editVisible" />
  </div>
</template>

<script lang="ts" setup>
import {definePageMeta, onMounted, reactive, ref} from "#imports";
import {DataTableColumns, NAvatar, NButton, NImage, PaginationInfo, PaginationProps} from "naive-ui";
import {$fetch} from "ohmyfetch";
import EditHallModal from "~/components/hall/EditHallModal.vue";
import {Ref} from "@vue/reactivity";

definePageMeta({
  name: "index",
  layout: "admin"
})
const data = ref([] as Room[])
const editVisible = ref(false)
const editId:Ref<null|number> = ref(null)
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
const handlePageChange = (page) => {
  getList(page)
}
const addHall = ()=>{
  editId.value = null
  editVisible.value = true
}
onMounted(() => getList())
const columns: DataTableColumns = [{
  title: "编号",
  key: "roomNo"
}, {
  title: "大厅名称",
  key: "name"
}, {
  title: "图标",
  key: "roomIcon",
  render(row) {
    return h(
        NImage,
        {
          style: {
            width: '40px',
            height: '40px'
          },
          src: row.roomIcon
        }
    )
  }
}, {
  title: "大厅标签",
  key: "tag.name"
}, {
  title: "创建时间",
  key: "createTime"
},{
  title: "操作",
  key: "action",
  render(row){
    return h(NButton,{
      text:true,
      tag:'a',
      type:"primary",
      onClick:()=>{
        editId.value = row.id as number
        editVisible.value = true
      }
    },'编辑')
  }
}]
</script>

<style scoped>

</style>
