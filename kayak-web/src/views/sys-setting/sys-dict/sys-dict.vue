<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="字典标识">
                    <Input v-model="searchData.dict" clearable placeholder="请输入..."/>
                </FormItem>
                <FormItem label="字典名称">
                    <Input v-model="searchData.dictname" clearable placeholder="请输入..."/>
                </FormItem>
                <div style="text-align: center;">
                    <FormItem>
                        <Button type="primary" @click="search">查询</Button>
                        <Button type="ghost" style="margin-left: 8px" @click="searchData={}">重置</Button>
                    </FormItem>
                </div>
            </Form>
        </Card>
        <div style="margin-bottom: 8px;">
            <Button type="primary" icon="plus" @click="add_dict_model=true">添加数字字典</Button>
        </div>
        <Row>
            <Col span="12">
                <Table :data="tableDictData" :columns="tableDictColumns" @on-row-click="reloadDictItemDatas" stripe></Table>
                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                        <Page :total="dict_total" :current="dict_start" @on-change="dictChangePage"></Page>
                    </div>
                </div>
            </Col>
            <Col span="12">
                <Table :data="tableDictItemData" :columns="tableDictItemColumns" stripe></Table>
                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                        <Page :total="dict_item_total" :current="dict_item_start" @on-change="dictItemChange"></Page>
                    </div>
                </div>
            </Col>
        </Row>

        <Modal v-model="add_dict_model" @on-visible-change="dictChange">
            <p slot="header" style="text-align:center">
                <span>添加数字字典</span>
            </p>
            <div style="text-align:center">
                <Form ref="addDictData" :label-width="80" :model="addDictData" :rules="validRule">
                    <FormItem label="字典标识" prop="dict">
                        <Input v-model="addDictData.dict" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="字典名称" prop="dictname">
                        <Input v-model="addDictData.dictname" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="cancleAdd">取消</Button>
                <Button type="primary"  @click="addDict">添加</Button>
            </div>
        </Modal>

        <Modal v-model="edit_dict_model">
            <p slot="header" style="text-align:center">
                <span>修改参数</span>
            </p>
            <div style="text-align:center">
                <Form ref="editDictData" :label-width="80" :model="editDictData" :rules="validRule">
                    <FormItem label="字典标识" prop="dict">
                        <Input v-model="editDictData.dict" disabled placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="字典名称" prop="dictname">
                        <Input v-model="editDictData.dictname" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="edit_dict_model=false">取消</Button>
                <Button type="primary"  @click="editDict">修改</Button>
            </div>
        </Modal>

        <Modal v-model="add_dict_item_model" @on-visible-change="dictItemChange">
            <p slot="header" style="text-align:center">
                <span>添加数字字典子项</span>
            </p>
            <div style="text-align:center">
                <Form ref="addDictItemData" :label-width="80" :model="addDictItemData" :rules="validItemRule">
                    <FormItem label="字典标识">
                        <Input v-model="select_dict" disabled placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="子项值" prop="itemkey">
                        <Input v-model="addDictItemData.itemkey" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="子项名" prop="itemval">
                        <Input v-model="addDictItemData.itemval" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="渲染样式">
                        <Input v-model="addDictItemData.itemrender" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="排序">
                        <Input v-model="addDictItemData.itemorder" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="cancleAddItem">取消</Button>
                <Button type="primary"  @click="addDictItem">添加</Button>
            </div>
        </Modal>

        <Modal v-model="edit_dict_item_model">
            <p slot="header" style="text-align:center">
                <span>修改数字字典子项</span>
            </p>
            <div style="text-align:center">
                <Form ref="editDictItemData" :label-width="80" :model="editDictItemData" :rules="validRule">
                    <FormItem label="字典标识">
                        <Input v-model="editDictItemData.dict" disabled placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="子项值" prop="itemkey">
                        <Input v-model="editDictItemData.itemkey" disabled placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="子项名" prop="itemval">
                        <Input v-model="editDictItemData.itemval" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="渲染样式">
                        <Input v-model="editDictItemData.itemrender" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="排序">
                        <Input v-model="editDictItemData.itemorder" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="edit_dict_item_model=false">取消</Button>
                <Button type="primary"  @click="editDictItem">修改</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
export default {
    name: 'machine-group',
    data () {
        return {
            dict_start:1,
            dict_limit:10,
            dict_total:0,
            add_dict_model:false,
            edit_dict_model:false,
            searchData:{
                param_key:"",
                param_name:"",
            },
            addDictData:{},
            validRule: {
                dict: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                dictname: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ]
            },
            editDictData:{
                dict:"",
                dictname:"",
            },
            tableDictData: [],
            tableDictColumns: [
                {
                    title: '字典标识',
                    key: 'dict'
                },
                {
                    title: '字典名称',
                    key: 'dictname'
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
                                        this.kayak.util.clone(this.editDictData,params.row);
                                        this.edit_dict_model = true;
                                    }
                                }
                            }, '编辑'),
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
                                        this.select_dict = params.row.dict;
                                        this.add_dict_item_model = true;
                                    }
                                }
                            }, '添加子项'),
                            h('Poptip', {
                                props: {
                                    title: '您确认删除这条内容吗？',
                                    confirm:true
                                },
                                on: {
                                    "on-ok": () => {
                                        this.deleteDict(params.row);
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
            ],
            dict_item_start:1,
            dict_item_limit:10,
            dict_item_total:0,
            select_dict:"",
            add_dict_item_model:false,
            edit_dict_item_model:false,
            addDictItemData:{},
            validItemRule: {
                dict: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                itemkey: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                itemval: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ]
            },
            editDictItemData:{
                dict:"",
                itemkey:"",
                itemval:"",
                itemrender:"",
                itemorder:""
            },
            tableDictItemData: [],
            tableDictItemColumns: [
                {
                    title: '字典标识',
                    key: 'dict'
                },
                {
                    title: '子项值',
                    key: 'itemkey'
                },
                {
                    title: '子项名',
                    key: 'itemval'
                },
                {
                    title: '渲染样式',
                    key: 'itemrender',
                    render: (h, params) => {
                        return h("Tag",{
                            props:{
                                color:params.row.itemrender
                            }
                        },params.row.itemval);
                    }
                },
                {
                    title: '排序',
                    key: 'itemorder'
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
                                        this.kayak.util.clone(this.editDictItemData,params.row);
                                        this.edit_dict_item_model = true;
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
                                        this.deleteDictItem(params.row);
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
        loadDictDatas () {//查询数据
            this.kayak.httpUtil.comnQuery({exeid:"find_sys_dicts",method:"post",params:{"dict":this.searchData.dict,"dictname":this.searchData.dictname,"start":(this.dict_start-1)*this.dict_limit,"limit":this.dict_limit}}).then(data=>{
                this.tableDictData = data.rows;
                this.dict_total = data.results;
            });
        },
        dictChangePage (start) {
            this.dict_start = start;
            this.tableDictData();
        },

        search(){
           this.start = 1;
           this.loadDictDatas();
        },
        cancleAdd(){
          this.addDictData = {};
          this.add_dict_model = false;
        },
        cancleAddItem(){
            this.addDictItemData = {};
            this.add_dict_item_model = false;
        },
        addDict(){//添加参数
            this.$refs["addDictData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_dict",params:this.addDictData}).then(data=>{
                        this.add_dict_model = false;
                        this.loadDictDatas();
                    });
                }
            });
        },
        editDict(){//修改参数
            this.$refs["editDictData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_dict",params:this.editDictData}).then(data=>{
                        this.edit_dict_model = false;
                        this.loadDictDatas();
                    });
                }
            });
        },
        deleteDict(row){
            this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_dict",params:{"dict":row.dict}}).then(data=>{
                this.loadDictDatas();
            });
        },
        dictChange(re){
            if(!re){
                this.addDictData = {};
            }
        },

        loadDictItemDatas () {//查询数据
            this.kayak.httpUtil.comnQuery({exeid:"find_sys_dict_items",method:"post",params:{"dict":this.select_dict,"start":(this.dict_item_start-1)*this.dict_item_limit,"limit":this.dict_item_limit}}).then(data=>{
                this.tableDictItemData = data.rows;
                this.dict_item_total = data.results;
            });
        },
        reloadDictItemDatas(data){
            this.dict_item_start = 1;
            this.select_dict = data.dict;
            this.loadDictItemDatas();
        },
        dictChangePage (start) {
            this.dict_start = start;
            this.tableDictData();
        },
        dictItemChange(re){
            if(!re){
                this.addDictItemData = {};
            }
        },
        addDictItem(){//添加参数
            this.$refs["addDictItemData"].validate((valid) => {
                if (valid) {
                    this.addDictItemData.dict = this.select_dict;
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_dict_item",params:this.addDictItemData}).then(data=>{
                        this.add_dict_item_model = false;
                        this.loadDictItemDatas();
                    });
                }
            });
        },
        editDictItem(){//修改参数
            this.$refs["editDictItemData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_dict_item",params:this.editDictItemData}).then(data=>{
                        this.edit_dict_item_model = false;
                        this.loadDictItemDatas();
                    });
                }
            });
        },
        deleteDictItem(row){
            this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_dict_item",params:{"dict":row.dict,"itemkey":row.itemkey}}).then(data=>{
                this.loadDictItemDatas();
            });
        }
    },
    mounted () {
        this.loadDictDatas();
    },
};
</script>

