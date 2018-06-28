<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                查询项
            </p>
            <Form ref="queryData" label-position="right" :model="querydept" :inline="true" :label-width="80">
                <FormItem label="部门名称" prop="dept_name">
                    <Row>
                        <Col>
                            <Input v-model="querydept.dept_name" clearable placeholder="请输入..."></Input>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem label="备注" prop="dept_remark">
                    <Row>
                        <Col>
                            <Input v-model="querydept.dept_remark" clearable placeholder="请输入..."></Input>
                        </Col>
                    </Row>
                </FormItem>
                <div style="text-align: center;">
                    <FormItem>
                        <Button type="primary" @click="query">查询</Button>
                        <Button type="ghost" style="margin-left: 8px" @click="cleanqueryData">重置</Button>
                    </FormItem>
                </div>
            </Form>
        </Card>
        <div style="margin-bottom: 8px;">
            <Button type="primary" icon="plus" @click="add_model=true">添加部门</Button>
        </div>
        <Table :data="tableData" :columns="tableColumns" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
                <Page :showTotal="true" :total="total" :current="start" @on-change="changePage"></Page>
            </div>
        </div>
        <Modal v-model="add_model" >
            <p slot="header" style="text-align:center">
                <span>添加部门</span>
            </p>
            <div style="text-align:center">
                <Form ref="addData" :label-width="80" :model="addData" :rules="validate">
                    <FormItem label="名称" prop="dept_name">
                        <Row>
                            <Col span="16">
                                <Input v-model="addData.dept_name" clearable placeholder="请输入..."/>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="简称" prop="dept_ename">
                        <Row>
                            <Col span="16">
                                <Input v-model="addData.dept_ename" clearable placeholder="请输入..."/>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="备注" prop="dept_remark">
                        <Row>
                            <Col span="16">
                                <Input type="textarea" v-model="addData.dept_remark" clearable placeholder="请输入..." />
                            </Col>
                        </Row>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button type="primary"  @click="adddept">添加</Button>
                <Button @click="cleanadd">重置</Button>
            </div>
        </Modal>

        <Modal v-model="edit_model">
            <p slot="header" style="text-align:center">
                <span>修改部门</span>
            </p>
            <div style="text-align:center">
                <Form ref="editData" :label-width="80" :model="editData" :rules="validate">
                    <FormItem label="ID" >
                        <Row>
                            <Col span="16">
                                <Input v-model="editData.dept_id" disabled />
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="名称" prop="dept_name">
                        <Row>
                            <Col span="12">
                                <Input v-model="editData.dept_name" clearable placeholder="请输入..."/>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="简称" prop="dept_ename">
                        <Row>
                            <Col span="12">
                                <Input v-model="editData.dept_ename" clearable placeholder="请输入..."/>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="备注" prop="dept_remark">
                        <Row>
                            <Col span="16">
                                <Input type="textarea" v-model="editData.dept_remark" clearable placeholder="请输入..." />
                            </Col>
                        </Row>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button type="primary"  @click="moddept">修改</Button>
                <Button @click="edit_model=false">关闭</Button>
            </div>
        </Modal>

        <Modal v-model="set_dept_role">
            <p slot="header" style="text-align:center">
                <span>角色设置</span>
            </p>
            <div style="text-align:center">
                <Form ref="deptroleData" :label-width="80" :model="deptroleData" >
                    <FormItem label="ID" >
                        <Row>
                            <Col span="16">
                                <Input v-model="deptroleData.dept_id" disabled />
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="名称" prop="dept_name">
                        <Row>
                            <Col span="16">
                                <Input v-model="deptroleData.dept_name" disabled/>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem label="角色" >
                        <Row>
                            <Col span="16">
                                <ul class="padding-left: 0;">
                                    <li class="list-style-type: none;" v-for="(item,index) in roleidlist" >
                                        <span >{{item.role_name}}</span>
                                        <input type="checkbox"  v-model="item.role_check" @click.stop='chooseroleid(index)'/>
                                    </li>
                                </ul>
                            </Col>
                        </Row>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer" style="text-align: center;">
                <Button type="primary"  @click="setdeptrole">保存</Button>
                <Button @click="cleanchooseroleid">取消</Button>
            </div>
        </Modal>

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
                set_dept_role:false,
                add_model:false,
                edit_model:false,
                deptroleData:{
                    dept_id:'',
                    dept_name:''
                },
                roleidarray:[],
                roleidlist:[],
                editData:{
                    dept_id:'',
                    dept_ename:'',
                    dept_name:'',
                    dept_remark:''
                },
                addData:{
                    dept_name:'',
                    dept_remark:'',
                    dept_ename:'',
                },
                tableData: [],
                tableColumns: [
                    {
                        title: '名称',
                        key: 'dept_name'
                    },
                    {
                        title: '简称',
                        key: 'dept_ename'
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
                                h('Button', {
                                    props: {
                                        type: 'warning',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showdeptrole(params.row);
                                        }
                                    }
                                }, '角色设置'),
                                h('Poptip', {
                                    props: {
                                        title: '您确认删除这条内容吗？',
                                        confirm:true
                                    },
                                    on: {
                                        "on-cancel": () => {
                                        },
                                        "on-ok": () => {
                                            this.deletedept(params.row);
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
                validate: {
                    dept_name: [
                        {required: true, message: '名称不能为空！', trigger: 'blur'},
                    ],
                    dept_remark:[
                        {type: 'string', max: 256, message: '不能超过256个长度', trigger: 'blur' },
                    ],
                    dept_ename:[
                        {required: true, message: '名称不能为空！', trigger: 'blur'},
                    ]
                },
            }
        },
        methods:{
            query:function () {
                this.kayak.httpUtil.comnQuery({exeid:"find_sys_dept",method:"post",params:{"dept_name":this.querydept.dept_name,"dept_remark":this.querydept.dept_remark,"start":(this.start-1)*this.limit,"limit":this.limit}}).then(data=>{
                    this.tableData = data.rows;
                    this.total = data.results;
                });
            },
            cleanqueryData:function(){
                this.$refs['queryData'].resetFields();
            },
            changePage:function (start) {
                this.start = start;
                this.query();
            },
            cleanadd:function(){
                this.$refs['addData'].resetFields();
            },
            adddept:function () {
                let _this = this;
                this.$refs["addData"].validate((valid) => {
                    if (valid) {
                        this.kayak.httpUtil.comnUpdate({method:"post",exeid:"add_sys_dept",params:this.addData}).then(data=>{
                            _this.add_model = false;
                            _this.query();
                        });
                    }
                });
            },
            moddept:function () {
                this.$refs["editData"].validate((valid) => {
                    if (valid) {
                        this.kayak.httpUtil.comnUpdate({method:"post",exeid:"update_sys_dept",params:this.editData}).then(data=>{
                            this.edit_model = false;
                            this.query();
                        });
                    }
                });
            },
            showdeptrole:function(row){
                let _this = this;
                this.kayak.httpUtil.query({url:"dept/finddeptroles.json",method:"post",params:{'dept_id':row.dept_id}}).then(data=>{
                    _this.set_dept_role = true;
                    _this.deptroleData.dept_id = row.dept_id;
                    _this.deptroleData.dept_name = row.dept_name;
                    _this.roleidlist = data.returndata.rows;
                    for(var i = 0 ;i< data.returndata.rows.length;i++){
                        if(data.returndata.rows[i].role_check === true){
                            _this.roleidarray.splice(0,_this.roleidarray.length);
                            _this.roleidarray.push(data.returndata.rows[i].role_id);
                        }
                    }
                });
            },
            deletedept:function (row) {
                this.kayak.httpUtil.comnUpdate({method:"post",exeid:"delete_sys_dept",params:{"dept_id":row.dept_id}}).then(data=>{
                    this.query();
                });
            },
            setdeptrole:function () {
                let s = document.getElementById('deptroleids');
                let _this = this;
                this.kayak.httpUtil.update({url:"dept/updatedeptroles.json",method:"post",params:{'dept_id':this.deptroleData.dept_id,'roleidarray':this.roleidarray}}).then(data=>{
                    _this.set_dept_role = false;
                    _this.query();
                });
            },
            chooseroleid:function (index) {
                if(this.roleidlist[index].role_check){
                      for(var i = 0;i<this.roleidarray.length;i++){
                          if(this.roleidlist[index].role_id == this.roleidarray[i]){
                              this.roleidarray.splice(i,1);
                          }
                      }
                }else{
                    this.roleidarray.push(this.roleidlist[index].role_id);
                }
            },
            cleanchooseroleid:function () {
                this.set_dept_role=false;
                this.roleidarray.splice(0,this.roleidarray.length);
            }
        },
        mounted(){
            this.query();
        }
    }
</script>

<style scoped>

</style>