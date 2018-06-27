<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="部门名称">
                    <Input v-model="querydept.dept_name" clearable placeholder="请输入..."></Input>
                </FormItem>
                <FormItem label="备注">
                    <Input v-model="querydept.dept_remark" clearable placeholder="请输入..."></Input>
                </FormItem>
                <div style="text-align: center;">
                    <FormItem>
                        <Button type="primary" @click="query">查询</Button>
                        <Button type="ghost" style="margin-left: 8px">重置</Button>
                    </FormItem>
                </div>
            </Form>
        </Card>
        <div style="margin-bottom: 8px;">
            <Button type="primary" icon="plus" @click="add_model=true">添加角色</Button>
        </div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="total" :current="start" @on-change="changePage"></Page>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "sys-department",
        data(){
            return{
                start:1,
                limit:10,
                total:0,
                querydept:{
                    dept_name:'',
                    dept_remark:'',
                },
                add_model:false,
                edit_model:false,
                editData:{
                    dept_id:'',
                    dept_name:'',
                    dept_remark:''
                },
                tableData: [],
                tableColumns: [
                    {
                        title: '名称',
                        key: 'dept_name'
                    },
                    {
                        title: '备注',
                        key: 'dept_remark'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.kayak.util.clone(this.editData,params.row);
                                            this.edit_model = true;
                                        }
                                    }
                                }, '编辑'),
                                h('Poptip', {
                                    props: {
                                        title: '您确认删除这条内容吗？',
                                        confirm:true
                                    },
                                    on: {
                                        "on-ok": () => {
                                            this.deleteRole(params.row);
                                        },
                                        "on-cancel": () => {
                                        }
                                    }
                                }, [h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    }
                                }, '删除')])
                            ]);
                        }
                    }
                ]
            }
        },
        methods:{
            query:function () {
                this.kayak.httpUtil.comnQuery({exeid:"find_sys_dept",method:"post",params:{"dept_name":this.querydept.dept_name,"dept_remark":this.querydept.dept_remark,"start":(this.start-1)*this.limit,"limit":this.limit}}).then(data=>{
                    this.tableData = data.rows;
                    this.total = data.results;
                });
            },
            changePage (start) {
                this.start = start;
                this.query();
            },
        },
        mounted(){

        }
    }
</script>

<style scoped>

</style>