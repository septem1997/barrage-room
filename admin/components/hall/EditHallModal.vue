<template>
  <n-modal
      preset="card"
      @update:show="handleClose"
      :title="`${id?'编辑':'新增'}大厅`"
      style="width: 480px"
      v-model:show="visible">
    <n-form>
      <n-form-item label="名称">
        <n-input v-model:value="hall.name"></n-input>
      </n-form-item>
      <n-form-item label="标签">
        <n-select :options="options" v-model:value="hall.tagId"></n-select>
      </n-form-item>
      <n-form-item label="图标">
        <n-image
            v-if="hall.roomIcon"
            :src="hall.roomIcon"
            style="width: 40px;height: 40px;margin-right: 10px"
        >

        </n-image>
        <n-upload
            :show-file-list="false"
            :custom-request="customRequest"
        >
          <n-button>上传图标</n-button>
        </n-upload>
      </n-form-item>
      <n-form-item style="direction: rtl">
        <n-button @click="saveHall">确定</n-button>
      </n-form-item>
    </n-form>
  </n-modal>
</template>

<script lang="ts" setup>
import {defineEmits} from "@vue/runtime-core";
import {reactive, ref, watch} from "#imports";
import {$fetch} from "ohmyfetch";
import {UploadCustomRequestOptions} from "naive-ui";

const emit = defineEmits(['update:visible','editSuccess'])
const props = defineProps({
  visible: Boolean,
  id: {
    type: Number
  }
})
const hall = reactive({})
const options = ref([])
const customRequest = async ({file}: UploadCustomRequestOptions) => {
  const formData = new FormData()
  formData.append('file', file.file as File)
  const res = await $fetch("/api/upload", {
    method: 'post',
    body: formData
  })
  hall.roomIcon = res.data
}
const saveHall = async () => {
  if (!hall.tagId) {
    return
  }
  await $fetch("/api/room/editHall", {
    method: 'post',
    body: hall
  })
  emit('editSuccess')
}
watch(() => props.visible, async (visible) => {
  if (visible) {
    let hallInfo = {
      name: '',
      tagId: null,
      roomIcon: null,
    }
    const res = await $fetch('/api/room/tags')
    options.value = res.data.map(item =>
        ({value: item.id, label: item.name}))
    if (props.id) {
      const res = await $fetch('/api/room/detail?id=' + props.id)
      hallInfo = res.data
      hallInfo.tagId = res.data.tag.id
    }
    Object.assign(hall, hallInfo)
  }
})
const handleClose = (visible) => {
  emit('update:visible', visible)
}
</script>

<style scoped>

</style>
