<style scoped>
    @import '../../styles/common.less';
    @import '../tables/components/table.less';
</style>

<template>
    <Card style="margin-bottom: 8px;padding-bottom: 0">
        <!--                     *****************************************************************************************************              -->
        <Row>
            <Form ref="querydata" :model="querydata" label-position="right" :inline="true" :label-width="80" :rules="queryValidate">
                <FormItem label="用户姓名" prop="username">
                    <Input v-model="querydata.username" clearable placeholder="请输入..."/>
                </FormItem>
                <FormItem label="所属部门" prop="userdept">
                    <Input v-model="querydata.userdept" clearable placeholder="请输入..."/>
                </FormItem>
                <FormItem label="用户状态" prop="userstatus" >
                    <Select v-model="querydata.userstatus" >
                        <Option value="N">正常</Option>
                        <Option value="F">冻结</Option>
                        <Option value="D">注销</Option>
                    </Select>
                </FormItem>
                <FormItem label="手机号码" prop="usermobileno">
                    <Input v-model="querydata.usermobileno" clearable placeholder="请输入..."/>
                </FormItem>
                <div style="text-align: center;">
                    <span @click="query('querydata')" style="margin: 0 10px;"><Button type="primary" icon="search">查询</Button></span>
                    <Button @click="clean('querydata')" type="ghost">取消</Button>
                </div>
            </Form>
        </Row>
        <Row>
            <div>
                <div style="margin-bottom: 8px;">
                    <Button type="primary" icon="plus" @click="showaddpage('adddata')">添加用户</Button>
                </div>
                <Table :loading="loading" :columns="columns" :data="data" ref="table"  border></Table>
                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                        <Page :showTotal="true" :total="totalCount" :current="pageNumber" :pageSize="pageSize" @on-change="changePage"/>
                    </div>
                </div>
            </div>
        </Row>
        <!--                     *****************************************************************************************************              -->
        <Row>
            <div>
                <Modal :width="900" v-model="infomodel" >
                    <p slot="header" style="text-align:center">
                        <span>详细信息</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="info" :label-width="120" label-position="left" :model="infodata">
                            <FormItem label="用户ID">
                                <Col span="12">
                                    <Input v-model="infodata.userid" disabled />
                                </Col>
                            </FormItem>
                            <FormItem label="登陆名">
                                <Col span="12">
                                <Input v-model="infodata.userloginnname" disabled />
                                </Col>
                            </FormItem>
                            <FormItem label="用户姓名">
                                <Col span="12">
                                <Input v-model="infodata.username" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="所属部门">
                                <Col span="12">
                                <Input v-model="infodata.userdept" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="记录类型">
                                <Col span="12">
                                <Select v-model="infodata.userstatus" disabled>
                                    <Option value="N">正常</Option>
                                    <Option value="F">冻结</Option>
                                    <Option value="D">注销</Option>
                                </Select>
                                </Col>
                            </FormItem>
                            <FormItem label="证件类型">
                                <Col span="12">
                                <Select v-model="infodata.useridtype" disabled>
                                    <Option value="1">身份证</Option>
                                </Select>
                                </Col>
                            </FormItem>
                            <FormItem label="证件号">
                                <Col span="12">
                                <Input v-model="infodata.userridno" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="性别">
                                <Col span="12">
                                <Select v-model="infodata.usersex" disabled>
                                    <Option value="0">女</Option>
                                    <Option value="1">男</Option>
                                </Select>
                                </Col>
                            </FormItem>
                            <FormItem label="手机号">
                                <Col span="12">
                                <Input v-model="infodata.usermobileno" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="办公电话">
                                <Col span="12">
                                <Input v-model="infodata.userofficeno" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="家庭电话">
                                <Col span="12">
                                <Input v-model="infodata.userhomeno" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="传真号">
                                <Col span="12">
                                <Input v-model="infodata.userfaxno" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="EMAIL">
                                <Col span="12">
                                <Input v-model="infodata.useremail" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="邮编">
                                <Col span="12">
                                <Input v-model="infodata.userpostcode" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="地址">
                                <Col span="12">
                                <Input v-model="infodata.useraddress" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="创建时间">
                                <Col span="12">
                                <Input v-model="infodata.createdate" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="修改时间">
                                <Col span="12">
                                <Input v-model="infodata.modifydate" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="密码错误次数">
                                <Col span="12">
                                <Input v-model="infodata.pwderrtimes" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="密码输错锁定时间">
                                <Col span="12">
                                <Input v-model="infodata.pwderrlockdt" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="密码错误时间">
                                <Col span="12">
                                <Input v-model="infodata.pwdsetdate" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="最后登录时间">
                                <Col span="12">
                                <Input v-model="infodata.lastlogintime" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="上次登录">
                                <Col span="12">
                                <Input v-model="infodata.lastloginstation" disabled/>
                                </Col>
                            </FormItem>
                            <FormItem label="用户证书">
                                <Col span="12">
                                <Input v-model="infodata.usercertificate" disabled/>
                                </Col>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="infoclean">关闭</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <!--                     *****************************************************************************************************              -->
        <Row>
            <div>
                <Modal :width="900" v-model="modmodel" >
                    <p slot="header" style="text-align:center" >
                        <span>修改资料</span>
                    </p>
                    <div>
                        <Form ref="moddata" :label-width="120" label-position="right" :model="moddata" :rules="modValidate">
                            <FormItem label="用户ID">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="moddata.userid" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="登陆名" >
                                <Row>
                                    <Col span="12">
                                    <Input v-model="moddata.userloginnname" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="用户姓名" prop="username">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="moddata.username" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="所属部门" prop="userdept">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="moddata.userdept" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="登陆状态" prop="userstatus" >
                                <Row>
                                    <Col span="12">
                                        <Select v-model="moddata.userstatus" clearable>
                                            <Option value="N">正常</Option>
                                            <Option value="F">冻结</Option>
                                            <Option value="D">注销</Option>
                                        </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="证件类型" prop="useridtype">
                                <Row>
                                    <Col span="12">
                                        <Select v-model="moddata.useridtype" clearable>
                                            <Option value="1">身份证</Option>
                                        </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="证件号" prop="userridno">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.userridno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="性别" prop="usersex">
                                <Row>
                                    <Col span="12">
                                        <Select v-model="moddata.usersex" clearable>
                                            <Option value="0">女</Option>
                                            <Option value="1">男</Option>
                                        </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="手机号" prop="usermobileno">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.usermobileno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="办公电话" prop="userofficeno">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.userofficeno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="家庭电话" prop="userhomeno">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.userhomeno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="传真号" prop="userfaxno">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.userfaxno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="EMAIL" prop="useremail">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.useremail" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="邮编" prop="userpostcode">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.userpostcode" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="地址" prop="useraddress">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.useraddress" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="mod('moddata')">修改</Button>
                        <Button @click="clean('moddata')">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <!--                     *****************************************************************************************************              -->
        <div>
            <Modal  v-model="outmodal" title="注销用户？" :loading="outloading" @on-cancel="loginoutcleanrow"  @on-ok="loginout" :closable="false">
                <p>您确定要注销该用户么？一旦注销该用户将无法登陆！！！！</p>
            </Modal>
        </div>
        <!--                     *****************************************************************************************************              -->
        <div>
            <Modal  v-model="delmodal" title="删除用户？" :loading="delloading" @on-cancel="delcleanrow"  @on-ok="delrow" :closable="false">
                <p>您确定要删除该用户么？一旦删除该用户将丢弃！！！！</p>
            </Modal>
        </div>
        <!--                     *****************************************************************************************************              -->
        <Row>
            <div>
                <Modal :width="900" v-model="addmodel" >
                    <p slot="header" style="text-align:center" >
                        <span>新增用户资料</span>
                    </p>
                    <div>
                        <Form ref="adddata" :label-width="120" label-position="right" :model="adddata" :rules="newValidate">
                            <FormItem label="登陆名" prop="userloginnname">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userloginnname" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="用户姓名" prop="username">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.username" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="所属部门" prop="userdept">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userdept" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="登陆状态" prop="userstatus" >
                                <Row>
                                    <Col span="12">
                                    <Select v-model="adddata.userstatus" clearable>
                                        <Option value="N">正常</Option>
                                        <Option value="F">冻结</Option>
                                        <Option value="D">注销</Option>
                                    </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="证件类型" prop="useridtype">
                                <Row>
                                    <Col span="12">
                                    <Select v-model="adddata.useridtype" clearable>
                                        <Option value="1">身份证</Option>
                                    </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="证件号" prop="userridno">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userridno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="性别" prop="usersex">
                                <Row>
                                    <Col span="12">
                                    <Select v-model="adddata.usersex" clearable>
                                        <Option value="0">女</Option>
                                        <Option value="1">男</Option>
                                    </Select>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="手机号" prop="usermobileno">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.usermobileno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="办公电话" prop="userofficeno">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userofficeno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="家庭电话" prop="userhomeno">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userhomeno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="传真号" prop="userfaxno">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userfaxno" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="EMAIL" prop="useremail">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.useremail" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="邮编" prop="userpostcode">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.userpostcode" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="地址" prop="useraddress">
                                <Row>
                                    <Col span="12">
                                    <Input v-model="adddata.useraddress" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="add('adddata')">新增</Button>
                        <Button @click="clean('adddata')">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
    </Card>
</template>

<script>
    export default {
        name: "userinfo",
        data() {
            const validateIsNumber = (rule, value, callback) => {
                var reg = /^[1-9]\d*$/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入正整数的数字!!!!!'));
                }else{
                    callback();
                }
            };

            const validateCheckLoginName = (rule, value, callback) => {
                let _this = this;
                let checek = {
                    userloginnname : ''
                }
                checek.userloginnname = value;
                let json = JSON.stringify(checek);
                var reg = /^[a-zA-Z0-9_-]{4,16}$/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入（字母，数字，下划线，减号）!!!!!'));
                    return
                }
                let flag = true;
                _this.$http.post(_this.httpurl.toString() + '/feigen/checkUserInfoLogname', _this.$qs.stringify({
                    json:json
                })).then(function (response) {
                    if(response.data.returnState === "0000" && response.data.detail >= 1){
                        callback(new Error('该登陆账号已经存在，请使用其他账号!!!!!'));
                        return
                    }else if(response.data.returnState !== "0000"){
                        callback(new Error(response.data.returnMsg));
                        return
                    }else{
                        callback();
                    }
                }).catch(function (error) {
                    callback(error);
                    return
                });
            };

            const validatePhone = (rule, value, callback) => {
                var reg =  /^1[3,4,5,7,8]\d{9}$/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入正确的手机号!!!!!'));
                }else{
                    callback();
                }
            };
            const validateTEl = (rule, value, callback) => {
                var reg =  /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入正确的电话号码!!!!!'));
                }else{
                    callback();
                }
            };
            const validateIsuPattern = (rule, value, callback) => {

            };
            const validateChinese = (rule, value, callback) => {
                var reg = /[\u4E00-\u9FA5]/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入汉子!!!!!'));
                }else{
                    callback();
                }
            };
            const validateIDNumber = (rule, value, callback) => {
                var reg = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入证件号!!!!!'));
                }else{
                    callback();
                }
            };
            const validateEmail = (rule, value, callback) => {
                var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
                if(!reg.test(value)&& value !== ""){
                    callback(new Error('请输入有效的email!!!!!'));
                }else{
                    callback();
                }
            };
            const validatePost = (rule, value, callback) => {
                var reg = /[1-9]{1}(\d+){5}/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请输入正确的邮编!!!!!'));
                }else{
                    callback();
                }
            };
            return {
                loading: true,
                outmodal:false,
                outloading: true,
                delmodal:false,
                delloading: true,
                addmodel: false,
                totalCount: 0,
                pageNumber: 1,
                pageSize: 20,
                infomodel:false,
                modmodel:false,
                data: [],
                querydata:{
                    username:'',
                    userdept:'',
                    userstatus:'',
                    usermobileno:''
                },
                columns: [
                    {
                        key: 'userid',
                        title: '用户id',
                        fixed: "left",
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userloginnname',
                        title: '登陆名',
                        fixed: "left",
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'username',
                        title: '用户名',
                        width: 150,
                        fixed: "left",
                        sortable: true
                    },
                    {
                        key: 'userdept',
                        title: '用户部门',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userstatus',
                        title: '用户状态',
                        width: 150,
                        sortable: true,
                        render: (h, params) => {
                            var user_status = params.row.userstatus;
                            if(user_status === 'N'){
                                return h("Tag",{props:{color:'green'} },"正常");
                            }else if(user_status === 'F'){
                                return h("Tag",{props:{color:'red'} },"冻结");
                            }else{
                                return h("Tag",{props:{color:'yellow'} },"注销");
                            }
                        }
                    },
                    {
                        key: 'useridtype',
                        title: '证件类型',
                        width: 150,
                        sortable: true,
                        render: (h, params) => {
                            var useridtype = params.row.useridtype;
                            if(useridtype === '1'){
                                return h("Tag",{props:{color:'blue'} },"身份证");
                            }
                        }
                    },
                    {
                        key: 'userridno',
                        title: '证件号码',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'usersex',
                        title: '性别',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'usermobileno',
                        title: '手机号',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userofficeno',
                        title: '办公电话',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userhomeno',
                        title: '家庭电话',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userfaxno',
                        title: '传真号',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'useremail',
                        title: '邮箱',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'userpostcode',
                        title: '邮政编码',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'useraddress',
                        title: '地址',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'createdate',
                        title: '创建时间',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'modifydate',
                        title: '修改时间',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'pwderrtimes',
                        title: '密码错误次数',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'pwderrlockdt',
                        title: '密码输错锁定时间',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'pwdsetdate',
                        title: '密码错误时间',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'lastlogintime',
                        title: '最后登录时间',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'lastloginstation',
                        title: '上次登陆IP',
                        width: 150,
                        sortable: true
                    },
                    {
                        key: 'usercertificate',
                        title: '证书编号',
                        width: 150,
                        sortable: true
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 240,
                        fixed: 'right',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'info',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.infoobj(params.row)
                                        }
                                    }}, '详细'),
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
                                            this.showmodpage(params.row);
                                        }
                                    }
                                }, '修改'),
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
                                            this.showoutpage(params.row);
                                        }
                                    }
                                }, '注销'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showdelpage(params.row);
                                        }
                                    }
                                }, '删除')
                            ]);
                        }
                    }
                ],
                queryValidate:{
                    usermobileno:[
                        {type: 'string', max: 11, message: '不能超过11个号码', trigger: 'blur' },
                        {type: 'string', min: 11, message: '不能小于11个号码', trigger: 'blur' },
                        {validator: validatePhone, trigger: 'blur' }
                    ]
                },
                newValidate:{
                    userloginnname:[
                        {required: true,message:'登陆名不能为空！', trigger: 'blur' },
                        {type: 'string', max: 16, message: '不能超过16个号码', trigger: 'blur' },
                        {type: 'string', min: 4, message: '不能小于4个号码', trigger: 'blur' },
                        {validator: validateCheckLoginName, trigger: 'blur' }
                    ],
                    username:[
                        {required: true,message:'请填写你的名字！', trigger: 'blur' },
                        {type: 'string', max: 8, message: '不能超过6个汉子', trigger: 'blur' },
                        {type: 'string', min: 2, message: '不能小于2个汉子', trigger: 'blur' },
                        {validator: validateChinese, trigger: 'blur' }
                    ],
                    userstatus:[
                        {required: true,message:'请编辑用户状态！', trigger: 'blur' },
                    ],
                    userridno:[
                        {type: 'string', max: 18, message: '不能超过18个数字', trigger: 'blur' },
                        {type: 'string', min: 18, message: '不能小于18个数字', trigger: 'blur' },
                        {validator: validateIDNumber, trigger: 'blur' }
                    ],
                    usermobileno:[
                        {type: 'string', max: 11, message: '不能超过11个号码', trigger: 'blur' },
                        {type: 'string', min: 11, message: '不能小于11个号码', trigger: 'blur' },
                        {validator: validatePhone, trigger: 'blur' }
                    ],
                    userofficeno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    userhomeno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    userfaxno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', mix: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    useremail:[
                        {type: 'string', max: 40, message: '不能超过40个长度', trigger: 'blur' },
                        {type: 'string', min: 4, message: '不能小于4个长度', trigger: 'blur' },
                        {validator: validateEmail, trigger: 'blur' }
                    ],
                    userpostcode:[
                        {type: 'string', max: 8, message: '不能超过8个数字', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个数字', trigger: 'blur' },
                        {validator: validatePost, trigger: 'blur' }
                    ],
                    useraddress:[
                        {type: 'string', max: 64, message: '不能超过64个汉子', trigger: 'blur' },
                        {type: 'string', min: 2, message: '不能小于2个汉子', trigger: 'blur' },
                        {validator: validateChinese, trigger: 'blur' }
                    ]
                },
                modValidate:{
                    username:[
                        {required: true,message:'请填写你的名字！', trigger: 'blur' },
                        {type: 'string', max: 8, message: '不能超过6个汉子', trigger: 'blur' },
                        {type: 'string', min: 2, message: '不能小于2个汉子', trigger: 'blur' },
                        {validator: validateChinese, trigger: 'blur' }
                    ],
                    userstatus:[
                        {required: true,message:'请编辑用户状态！', trigger: 'blur' },
                    ],
                    userridno:[
                        {type: 'string', max: 18, message: '不能超过18个数字', trigger: 'blur' },
                        {type: 'string', min: 18, message: '不能小于18个数字', trigger: 'blur' },
                        {validator: validateIDNumber, trigger: 'blur' }
                    ],
                    usermobileno:[
                        {type: 'string', max: 11, message: '不能超过11个号码', trigger: 'blur' },
                        {type: 'string', min: 11, message: '不能小于11个号码', trigger: 'blur' },
                        {validator: validatePhone, trigger: 'blur' }
                    ],
                    userofficeno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    userhomeno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    userfaxno:[
                        {type: 'string', max: 18, message: '不能超过18个号码', trigger: 'blur' },
                        {type: 'string', mix: 6, message: '不能小于6个号码', trigger: 'blur' },
                        {validator: validateTEl, trigger: 'blur' }
                    ],
                    useremail:[
                        {type: 'string', max: 40, message: '不能超过40个长度', trigger: 'blur' },
                        {type: 'string', min: 4, message: '不能小于4个长度', trigger: 'blur' },
                        {validator: validateEmail, trigger: 'blur' }
                    ],
                    userpostcode:[
                        {type: 'string', max: 8, message: '不能超过8个数字', trigger: 'blur' },
                        {type: 'string', min: 6, message: '不能小于6个数字', trigger: 'blur' },
                        {validator: validatePost, trigger: 'blur' }
                    ],
                    useraddress:[
                        {type: 'string', max: 64, message: '不能超过64个汉子', trigger: 'blur' },
                        {type: 'string', min: 2, message: '不能小于2个汉子', trigger: 'blur' },
                        {validator: validateChinese, trigger: 'blur' }
                    ]
                },
                infodata:{
                    userid: '',
                    userloginnname: '',
                    username: '',
                    userdept: '',
                    userstatus: '',
                    useridtype: '',
                    userridno: '',
                    usersex: '',
                    usermobileno: '',
                    userofficeno: '',
                    userhomeno: '',
                    userfaxno: '',
                    useremail: '',
                    userpostcode: '',
                    useraddress: '',
                    createdate: '',
                    modifydate: '',
                    pwderrtimes: '',
                    pwderrlockdt: '',
                    pwdsetdate: '',
                    lastlogintime: '',
                    lastloginstation: '',
                    usertype: '',
                    usercertificate: ''
                },
                moddata:{
                    userid: '',
                    userloginnname: '',
                    username: '',
                    userdept: '',
                    userstatus: '',
                    useridtype: '',
                    userridno: '',
                    usersex: '',
                    usermobileno: '',
                    userofficeno: '',
                    userhomeno: '',
                    userfaxno: '',
                    useremail: '',
                    userpostcode: '',
                    useraddress: ''
                },
                adddata:{
                    userloginnname: '',
                    username: '',
                    userdept: '',
                    userstatus: '',
                    useridtype: '',
                    userridno: '',
                    usersex: '',
                    usermobileno: '',
                    userofficeno: '',
                    userhomeno: '',
                    userfaxno: '',
                    useremail: '',
                    userpostcode: '',
                    useraddress: ''
                },
                outdata:{
                    userid:'',
                },
                deldata:{
                    userid:'',
                },

            }
        },
        methods: {
            query(name) {
                //OK
                let _this = this;
                let str = JSON.stringify(this.querydata);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$http.post(this.httpurl.toString() + '/feigen/getUserInfoList', this.$qs.stringify({
                            pageNumber: this.pageNumber,
                            pageSize: this.pageSize,
                            json:str
                        })).then(function (response) {
                            if(response.data.returnState === "0000"){
                                _this.pageNumber = response.data.pageNumber;
                                _this.data = response.data.detail;
                                _this.totalCount = response.data.totalCount;
                                _this.pageSize = response.data.pageSize;
                                _this.loading = false;
                                _this.$Message.success('查询成功!');
                            }else{
                                _this.$Message.error(response.data.returnMsg);
                            }
                        }).catch(function (error) {
                            if(typeof(error.response) == "undefined"){
                                _this.$Message.error("错误信息：" + error);
                            }else{
                                _this.$Message.error("错误信息：" + error.response.data.message);
                            }
                            console.log(error);
                        });

                    } else {
                        this.$Message.error('请先输入正确的数据!');
                    }
                });
            },
            showmodpage(row){
                //ok
                this.modmodel = true;
                this.moddata.userid =row.userid;
                this.moddata.userloginnname=row.userloginnname;
                this.moddata.username=row.username;
                this.moddata.userdept=row.userdept;
                this.moddata.userstatus=row.userstatus;
                this.moddata.useridtype=row.useridtype;
                this.moddata.userridno=row.userridno;
                this.moddata.usersex=row.usersex;
                this.moddata.usermobileno=row.usermobileno;
                this.moddata.userofficeno=row.userofficeno;
                this.moddata.userhomeno=row.userhomeno;
                this.moddata.userfaxno=row.userfaxno;
                this.moddata.useremail=row.useremail;
                this.moddata.userpostcode=row.userpostcode;
                this.moddata.useraddress=row.useraddress;
            },
            mod(name){
                //ok
                let _this = this;
                let str = JSON.stringify(this.moddata);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$http.post(this.httpurl.toString() + '/feigen/UpdataUserInfo', this.$qs.stringify({
                            json:str
                        })).then(function (response) {
                            if(response.data.returnState === "0000" && response.data.detail === 1){
                                _this.$Message.success('修改成功!');
                                _this.modmodel = false;
                                _this.query('querydata');
                            }else if(response.data.returnState === "0000" && response.data.detail !== 1){
                                this.$Message.success('修改失败,没有匹配的记录');
                            }else{
                                _this.$Message.error(response.data.returnMsg);
                            }
                        }).catch(function (error) {
                            if(typeof(error.response) == "undefined"){
                                _this.$Message.error("错误信息：" + error);
                            }else{
                                _this.$Message.error("错误信息：" + error.response.data.message);
                            }
                        });
                    } else {
                        this.$Message.error('请先输入正确的数据格式!');
                    }
                })
            },
            clean(name){
                this.$refs[name].resetFields();
            },
            infoobj(row){
                this.infodata.userid =row.userid;
                this.infodata.userloginnname=row.userloginnname;
                this.infodata.username=row.username;
                this.infodata.userdept=row.userdept;
                this.infodata.userstatus=row.userstatus;
                this.infodata.useridtype=row.useridtype;
                this.infodata.userridno=row.userridno;
                this.infodata.usersex=row.usersex;
                this.infodata.usermobileno=row.usermobileno;
                this.infodata.userofficeno=row.userofficeno;
                this.infodata.userhomeno=row.userhomeno;
                this.infodata.userfaxno=row.userfaxno;
                this.infodata.useremail=row.useremail;
                this.infodata.userpostcode=row.userpostcode;
                this.infodata.useraddress=row.useraddress;
                this.infodata.usercertificate=row.usercertificate;
                this.infodata.createdate=row.createdate;
                this.infodata.modifydate=row.modifydate;
                this.infodata.pwderrtimes=row.pwderrtimes;
                this.infodata.pwderrlockdt=row.pwderrlockdt;
                this.infodata.pwdsetdate=row.pwdsetdate;
                this.infodata.lastlogintime=row.lastlogintime;
                this.infodata.lastloginstation=row.lastloginstation;
                this.infodata.usertype=row.usertype;
                this.infodata.usercertificate=row.usercertificate;
                this.infomodel = true;
            },
            showoutpage(row){
                //ok
               this.outmodal = true;
               this.outdata.userid = row.userid;
            },
            showdelpage(row){
                this.delmodal = true;
                this.deldata.userid = row.userid;
            },
            changePage(pageNumber) {
                this.pageNumber = pageNumber;
                this.query('querydata');
            },
            showaddpage(name){
                this.addmodel = true;
                this.$refs[name].resetFields();
            },
            loginout(){
                //ok
                let _this = this;
                let json = JSON.stringify(this.outdata);
                this.$http.post(this.httpurl.toString() + '/feigen/changeStatus', this.$qs.stringify({
                    json:json
                })).then(function (response) {
                    if(response.data.returnState === "0000" && response.data.detail === 1){
                        _this.$Message.success("注销成功!");
                        _this.outmodal = false;
                        _this.query('querydata');
                    }else if(response.data.returnState === "0000" && response.data.detail !== 1){
                        this.$Message.success('修改失败,没有匹配的记录');
                    }else{
                        _this.$Message.error(response.data.returnMsg);
                    }
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },
            delcleanrow(){
                this.deldata.userid = '';
                this.delmodal = false;
            },
            loginoutcleanrow(){
                //okd
                this.outdata.userid = '';
                this.outmodal = false;
            },
            delrow(){
                //ok
                let _this = this;
                let json = JSON.stringify(this.deldata);
                this.$http.post(this.httpurl.toString() + '/feigen/delUserInfo', this.$qs.stringify({
                    json:json
                })).then(function (response) {
                    if(response.data.returnState === "0000" && response.data.detail === 1){
                        _this.$Message.success("删除成功!");
                        _this.delmodal = false;
                        _this.query('querydata');
                    }else if(response.data.returnState === "0000" && response.data.detail !== 1){
                        this.$Message.success('修改失败,没有匹配的记录');
                    }else{
                        _this.$Message.error(response.data.returnMsg);
                    }
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },
            add(name){
                let _this = this;
                let str = JSON.stringify(this.adddata);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$http.post(this.httpurl.toString() + '/feigen/saveUserInfo', this.$qs.stringify({
                            json:str
                        })).then(function (response) {
                            if(response.data.returnState === "0000" && response.data.detail === 1){
                                _this.$Message.success('新增成功!');
                                _this.modmodel = false;
                                _this.query('querydata');
                            }else if(response.data.returnState === "0000" && response.data.detail !== 1){
                                this.$Message.success('修改失败,没有匹配的记录');
                            }else{
                                _this.$Message.error(response.data.returnMsg);
                            }
                        }).catch(function (error) {
                            if(typeof(error.response) == "undefined"){
                                _this.$Message.error("错误信息：" + error);
                            }else{
                                _this.$Message.error("错误信息：" + error.response.data.message);
                            }
                        });
                    } else {
                        this.$Message.error('请先输入正确的数据!');
                    }
                });


            },
            infoclean(){
                this.infomodel = false;
            }
        },
        mounted() {
            this.query('querydata');
        }
    }
</script>

