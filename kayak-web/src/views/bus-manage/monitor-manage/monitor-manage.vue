<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="名称">
                    <Input v-model="searchData.developer_name" clearable placeholder="请输入..."></Input>
                </FormItem>
                <div style="text-align: center;">
                    <FormItem>
                        <Button type="primary" @click="search">查询</Button>
                        <Button type="ghost" style="margin-left: 8px">重置</Button>
                    </FormItem>
                </div>
            </Form>


        </Card>
    </div>
</template>

<script>
export default {
    name: 'monitor-manage',
    data () {
        return {
            searchData:{}
        };
    },
    methods: {
    },
    mounted () {
    },
};
</script>

