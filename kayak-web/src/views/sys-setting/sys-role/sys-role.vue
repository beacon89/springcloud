<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="名称">
                    <Input v-model="searchData.role_name" clearable placeholder="请输入..."></Input>
                </FormItem>
                <FormItem label="备注">
                    <Input v-model="searchData.role_remark" clearable placeholder="请输入..."></Input>
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
            <Button type="primary" icon="plus" @click="add_model=true">添加角色</Button>
        </div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :total="total" :current="start" @on-change="changePage"></Page>
            </div>
        </div>

        <Modal v-model="add_model" @on-visible-change="change">
            <p slot="header" style="text-align:center">
                <span>添加角色</span>
            </p>
            <div style="text-align:center">
                <Form ref="addData" :label-width="80" :model="addData" :rules="validRule">
                    <FormItem label="名称" prop="role_name">
                        <Input v-model="addData.role_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="addData.role_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="cancleAdd">取消</Button>
                <Button type="primary"  @click="addRole">添加</Button>
            </div>
        </Modal>

        <Modal v-model="edit_model">
            <p slot="header" style="text-align:center">
                <span>修改角色</span>
            </p>
            <div style="text-align:center">
                <Form ref="editData" :label-width="80" :model="editData" :rules="validRule">
                    <FormItem label="名称" prop="role_name">
                        <Input v-model="editData.role_name" clearable placeholder="请输入..."/>
                    </FormItem>
                    <FormItem label="备注">
                        <Input v-model="editData.role_remark" clearable placeholder="请输入..."/>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="edit_model=false">取消</Button>
                <Button type="primary"  @click="editRole">修改</Button>
            </div>
        </Modal>

        <Modal v-model="set_menu_model">
            <p slot="header" style="text-align:center">
                <span>权限设置</span>
            </p>
            <div>
                <Tree ref="menuDatas" :data="menuDatas" multiple></Tree>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button @click="set_menu_model=false">取消</Button>
                <Button type="primary"  @click="setRoleMenu">保存</Button>
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
            select_role_id:"",
            add_model:false,
            edit_model:false,
            set_menu_model:false,
            searchData:{},
            addData:{},
            menuDatas:[],
            validRule: {
                role_name: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ],
                role_remark: [
                    { required: true, message: '该项不能为空', trigger: 'blur' }
                ]
            },
            editData:{
                role_id:"",
                role_name:"",
                role_remark:""
            },
            tableData: [],
            tableColumns: [
                {
                    title: '名称',
                    key: 'role_name'
                },
                {
                    title: '备注',
                    key: 'role_remark'
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
                                        this.kayak.util.clone(this.editData,params.row);
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
                                        this.select_role_id = params.row.role_id;
                                        this.menuDatas = [];
                                        var _this = this;
                                        this.kayak.httpUtil.comnQueryTree({exeid:"find_sys_menus",method:"post",params:{}}).then(data=>{
                                            var rows = data.rows;
                                            this.kayak.httpUtil.comnQuery({exeid:"find_sys_role_menus",method:"post",params:{"role_id":this.select_role_id}}).then(data2=>{
                                                var roleMenus = data2.rows;
                                                rows.map(menu=>{
                                                    var menuData = {};
                                                    menuData.title = menu.text;
                                                    menuData.menu_id = menu.id;
                                                    menuData.expand = true;
                                                    menuData.selected = false;
                                                    menuData.children = [];

                                                    roleMenus.map(roleMenu=>{
                                                        if(roleMenu.menu_id == menuData.menu_id){
                                                            menuData.selected = true;
                                                        }
                                                    });

                                                    if(menu.children && menu.children.length > 0){
                                                        menu.children.map(subMenu=>{
                                                            var subMenuData = {};
                                                            subMenuData.title = subMenu.text;
                                                            subMenuData.menu_id = subMenu.id;
                                                            subMenuData.expand = true;
                                                            subMenuData.selected = false;
                                                            subMenuData.children = [];

                                                            roleMenus.map(roleMenu=>{
                                                                if(roleMenu.menu_id == subMenuData.menu_id){
                                                                    subMenuData.selected = true;
                                                                }
                                                            });

                                                            menuData.children.push(subMenuData);
                                                        });
                                                    }
                                                    _this.menuDatas.push(menuData);
                                                });
                                            });
                                        });
                                        this.set_menu_model = true;
                                    }
                                }
                            }, '权限设置'),
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
        };
    },
    methods: {
        loadDatas () {//查询数据
            this.kayak.httpUtil.comnQuery({exeid:"find_sys_roles",method:"post",params:{"role_name":this.searchData.role_name,"role_remark":this.searchData.role_remark,"start":(this.start-1)*this.limit,"limit":this.limit}}).then(data=>{
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
        addRole(){//添加角色
            this.$refs["addData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_role",params:this.addData}).then(data=>{
                        this.add_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        editRole(){//修改角色
            this.$refs["editData"].validate((valid) => {
                if (valid) {
                    this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_role",params:this.editData}).then(data=>{
                        this.edit_model = false;
                        this.loadDatas();
                    });
                }
            });
        },
        deleteRole(row){
            this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_role",params:{"role_id":row.role_id}}).then(data=>{
                this.loadDatas();
            });
        },
        change(re){
            if(!re){
                this.addData = {};
            }
        },
        setRoleMenu(){//修改角色
            var selectNodes = this.$refs["menuDatas"].getSelectedNodes();

            var menus = "";
            if(selectNodes && selectNodes.length > 0){
                for(var i=0; i<selectNodes.length; i++){
                    if(i == 0){
                        menus = selectNodes[i].menu_id;
                    }else{
                        menus += "," + selectNodes[i].menu_id;
                    }
                }
            }
            this.kayak.httpUtil.update({url:"menu/setRoleMenus.json",method:"post",params:{"role_id":this.select_role_id,"menus":menus}}).then(data=>{
                this.set_menu_model = false;
            });
        }
    },
    mounted () {
        this.loadDatas();
    },
};
</script>

