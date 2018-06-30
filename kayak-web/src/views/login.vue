<style lang="less">
    @import './login.less';
</style>

<template>
    <div class="login" @keydown.enter="handleSubmit">
        <div class="login-con">
            <Card :bordered="false">
                <p slot="title">
                    <Icon type="log-in"></Icon>
                    欢迎登录
                </p>
                <div class="form-con">
                    <Form ref="loginForm" :model="form" :rules="rules">
                        <FormItem prop="userName">
                            <Input v-model="form.userName" placeholder="请输入用户名">
                                <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="form.password" placeholder="请输入密码">
                                <span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem>
                            <Button @click="handleSubmit" type="primary" long>登录</Button>
                        </FormItem>
                    </Form>
                    <p class="login-tip">输入任意用户名和密码即可</p>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie';
export default {
    data () {
        return {
            form: {
                userName: 'admin',
                password: '123'
            },
            rules: {
                userName: [
                    { required: true, message: '账号不能为空', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        handleSubmit () {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    var password = this.form.password;
                    password = this.kayak.md5.hex_md5(password+"kayak2018");
                    this.kayak.httpUtil.query({url:"login.json",method:"post",params:{"username":this.form.userName,"password":password}}).then(data=>{
                        localStorage.setItem("authorization",data.returndata.token);
                        localStorage.setItem("password",password);
                        sessionStorage.setItem('username',this.form.userName);
                        sessionStorage.setItem('deptid',data.returndata.deptid);
                        sessionStorage.setItem('deptname',data.returndata.deptname);
                        sessionStorage.setItem('roleid',data.returndata.roleid);
                        sessionStorage.setItem('roletype',data.returndata.roletype);
                        Cookies.set('user', this.form.userName);
                        this.$store.commit('clearAllTags');
                        this.$router.push({
                            name: 'home_index'
                        });
                    });
                }
            });
        }
    }
};
</script>

<style>

</style>
