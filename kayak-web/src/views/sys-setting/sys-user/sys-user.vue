<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="名称">
                    <Input v-model="searchData.user_name" clearable placeholder="请输入..."/>
                </FormItem>
                <FormItem label="状态">
                    <Select v-model="searchData.user_status" style="width:200px">
                        <Option value="0" key="0">全部</Option>
                        <Option v-for="item in sys_user_status" :value="item.itemkey" :key="item.itemkey">{{ item.itemval }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="角色">
                    <Select v-model="searchData.role_id" style="width:200px">
                        <Option value="0" key="0">全部</Option>
                        <Option v-for="item in sys_roles" :value="item.role_id" :key="item.role_id">{{ item.role_name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="备注">
                    <Input v-model="searchData.user_remark" clearable placeholder="请输入..."/>
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
            <Button type="primary" icon="plus" @click="add_model=true">添加用户</Button>
        </div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="total" :current="start" @on-change="changePage"></Page>
            </div>
        </div>

        <Modal v-model="add_model" @on-visible-change="change">
            <p slot="header" style="text-align:center">
                <span>添加用户</span>
            </p>
            <div style="text-align:center">
                <Form ref="addData" :label-width="80" :model="addData" :rules="validRule">
                    <FormItem label="名称" prop="user_name">
                        <Input v-model="addData.user_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="角色">
                        <Select v-model="addData.role_id">
                            <Option v-for="item in sys_roles" :value="item.role_id" :key="item.role_id">{{ item.role_name }}</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="密码" prop="user_password">
                        <Input v-model="addData.user_password" type="password" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="状态">
                        <Select v-model="addData.user_status">
                            <Option v-for="item in sys_user_status" :value="item.itemkey" :key="item.itemkey">{{ item.itemval }}</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="addData.user_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="cancleAdd">取消</Button>
                <Button type="primary"  @click="addUser">添加</Button>
            </div>
        </Modal>

        <Modal v-model="edit_model">
            <p slot="header" style="text-align:center">
                <span>修改用户</span>
            </p>
            <div style="text-align:center">
                <Form ref="editData" :label-width="80" :model="editData" :rules="validRule">
                    <FormItem label="名称" prop="user_name">
                        <Input v-model="editData.user_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="角色">
                        <Select v-model="editData.role_id">
                            <Option v-for="item in sys_roles" :value="item.role_id" :key="item.role_id">{{ item.role_name }}</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="状态">
                        <Select v-model="editData.user_status">
                            <Option v-for="item in sys_user_status" :value="item.itemkey" :key="item.itemkey">{{ item.itemval }}</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="editData.user_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="edit_model=false">取消</Button>
                <Button type="primary"  @click="editUser">修改</Button>
            </div>
        </Modal>

        <Modal v-model="rest_model">
            <p slot="header" style="text-align:center">
                <span>重置密码</span>
            </p>
            <div style="text-align:center">
                <Form ref="restData" :label-width="80" :model="restData" :rules="validRule">
                    <FormItem label="密码" prop="user_password">
                        <Input v-model="restData.user_password" type="password" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="确认密码" prop="user_password2">
                        <Input v-model="restData.user_password2" type="password" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="rest_model=false">取消</Button>
                <Button type="primary"  @click="restPassword">修改</Button>
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
            rest_model:false,
            searchData:{},
            sys_user_status:[],
            sys_roles:[],
            addData:{},
            validRule: {
                role_id: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                user_name: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                user_password: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                user_password2: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                user_status: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ]
            },
            editData:{
                user_id:"",
                role_id:"",
                user_name:"",
                user_status:"",
                user_remark:""
            },
            restData:{},
            tableData: [],
            tableColumns: [
                {
                    title: '名称',
                    key: 'user_name'
                },
                {
                    title: '角色',
                    key: 'role_name'
                },
                {
                    title: '状态',
                    key: 'user_status',
                    render: (h, params) => {
                        var user_status = params.row.user_status;
                        for(var i=0; i< this.sys_user_status.length; i++){
                            if(this.sys_user_status[i].itemkey == user_status){
                                return h("Tag",{
                                    props:{
                                        color:this.sys_user_status[i].itemrender
                                    }
                                },this.sys_user_status[i].itemval);
                            }
                        }
                    }
                },
                {
                    title: '备注',
                    key: 'user_remark'
                },
                {
                    title: '操作',
                    key: 'remark',
                    render: (h, user) => {
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
                                        this.kayak.util.clone(this.editData,user.row);
                                        this.edit_model = true;
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
                                        this.restData = {user_id:""};
                                        this.kayak.util.clone(this.restData,user.row);
                                        this.rest_model = true;
                                    }
                                }
                            }, '重置密码'),
                            h('Poptip', {
                                props: {
                                    title: '您确认删除这条内容吗？',
                                    confirm:true
                                },
                                on: {
                                    "on-ok": () => {
                                        this.deleteUser(user.row);
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
            this.kayak.httpUtil.comnQuery({exeid:"find_sys_users",method:"post",params:{"user_name":this.searchData.user_name,"user_remark":this.searchData.user_remark,"start":(this.start-1)*this.limit,"limit":this.limit}}).then(data=>{
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
          this.addData = {};
          this.add_model = false;
        },
        addUser(){//添加用户
            this.$refs["addData"].validate((valid) => {
                if (valid) {
                    var user_password = this.addData.user_password;
                    user_password = this.kayak.md5.hex_md5(user_password+"kayak2018");
                    this.addData.user_password = user_password;

                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_user",params:this.addData}).then(data=>{
                        this.add_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        editUser(){//修改用户
            this.$refs["editData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_user",params:this.editData}).then(data=>{
                        this.edit_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        restPassword(){//修改用户
            this.$refs["restData"].validate((valid) => {
                if (valid) {
                    if(this.restData.user_password != this.restData.user_password2){
                        this.$Message.error("密码与确认密码不一致");
                        return;
                    }

                    var user_password = this.restData.user_password;
                    user_password = this.kayak.md5.hex_md5(user_password+"kayak2018");
                    this.restData.user_password = user_password;
                    this.restData.user_password2 = "";

                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_user_password",params:this.restData}).then(data=>{
                        this.rest_model = false;
                    });
                }
            });
        },
        deleteUser(row){
            this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_user",params:{"user_id":row.user_id}}).then(data=>{
                this.loadDatas();
            });
        },
        change(re){
            if(!re){
                this.addData = {};
            }
        }
    },
    mounted () {
        this.kayak.dict.dict("sys_user_status").then(data=>{
            this.sys_user_status = data;
            this.loadDatas();
        });

        this.kayak.httpUtil.comnQuery({exeid:"find_sys_roles",method:"post",params:{}}).then(data=>{
            this.sys_roles = data.rows;
        });
    },
};
</script>

