<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="主键">
                    <Input v-model="searchData.param_key" clearable placeholder="请输入..."></Input>
                </FormItem>
                <FormItem label="名称">
                    <Input v-model="searchData.param_name" clearable placeholder="请输入..."></Input>
                </FormItem>
                <div style="text-align: center;">
                    <FormItem>
                        <Button type="primary" @click="search">查询</Button>
                        <Button type="ghost" style="margin-left: 8px">重置</Button>
                    </FormItem>
                </div>
            </Form>
        </Card>
        <div style="margin-bottom: 8px;">
            <Button type="primary" icon="plus" @click="add_model=true">添加参数</Button>
        </div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="total" :current="start" @on-change="changePage"></Page>
            </div>
        </div>

        <Modal v-model="add_model" @on-visible-change="change">
            <p slot="header" style="text-align:center">
                <span>添加参数</span>
            </p>
            <div style="text-align:center">
                <Form ref="addParamsData" :label-width="80" :model="addParamsData" :rules="validRule">
                    <FormItem label="主键" prop="param_key">
                        <Input v-model="addParamsData.param_key" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="名称" prop="param_name">
                        <Input v-model="addParamsData.param_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="值" prop="param_value">
                        <Input v-model="addParamsData.param_value" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="addParamsData.param_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="cancleAdd">取消</Button>
                <Button type="primary"  @click="addParams">添加</Button>
            </div>
        </Modal>

        <Modal v-model="edit_model">
            <p slot="header" style="text-align:center">
                <span>修改参数</span>
            </p>
            <div style="text-align:center">
                <Form ref="editParamsData" :label-width="80" :model="editParamsData" :rules="validRule">
                    <FormItem label="主键" prop="param_key">
                        <Input v-model="editParamsData.param_key" readonly placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="名称" prop="param_name">
                        <Input v-model="editParamsData.param_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="值" prop="param_value">
                        <Input v-model="editParamsData.param_value" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="editParamsData.param_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="edit_model=false">取消</Button>
                <Button type="primary"  @click="editParams">修改</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
export default {
    name: 'machine-group',
    data () {
        return {
            start:1,
            limit:10,
            total:0,
            add_model:false,
            edit_model:false,
            searchData:{
                param_key:"",
                param_name:"",
            },
            addParamsData:{
                param_key:"",
                param_name:"",
                param_value:"",
                param_remark:""
            },
            validRule: {
                param_key: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                param_name: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                param_value: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ]
            },
            editParamsData:{
                param_key:"",
                param_name:"",
                param_value:"",
                param_remark:""
            },
            tableData: [],
            tableColumns: [
                {
                    title: '主键',
                    key: 'param_key'
                },
                {
                    title: '名称',
                    key: 'param_name'
                },
                {
                    title: '值',
                    key: 'param_value'
                },
                {
                    title: '备注',
                    key: 'param_remark'
                },
                {
                    title: '操作',
                    key: 'remark',
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
                                        this.kayak.util.clone(this.editParamsData,params.row);
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
                                        this.deleteParams(params.row);
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
        };
    },
    methods: {
        loadDatas () {//查询数据
            this.kayak.httpUtil.comnQuery({exeid:"find_sys_params",method:"post",params:{"param_key":this.searchData.param_key,"param_name":this.searchData.param_name,"start":(this.start-1)*this.limit,"limit":this.limit}}).then(data=>{
                this.tableData = data.rows;
                this.total = data.results;
            });
        },
        changePage (start) {
            this.start = start;
            this.loadDatas();
        },

        search(){
           this.start = 1;
            this.loadDatas();
        },
        cancleAdd(){
          this.addParamsData = {};
          this.add_model = false;
        },
        addParams(){//添加参数
            this.$refs["addParamsData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_params",params:this.addParamsData}).then(data=>{
                        this.add_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        editParams(){//修改参数
            this.$refs["editParamsData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"edit_sys_params",params:this.editParamsData}).then(data=>{
                        this.edit_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        deleteParams(row){
            this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_params",params:{"param_key":row.param_key}}).then(data=>{
                this.loadDatas();
            });
        },
        change(re){
            if(!re){
                this.addParamsData = {};
            }
        }
    },
    mounted () {
        this.loadDatas();
    },
};
</script>

