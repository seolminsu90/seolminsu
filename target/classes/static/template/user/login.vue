<template>
  <section id="loginWrap">
    <p>
      <label>아이디</label>
      <input
        class="full"
        type="text"
        name="id"
        id="id"
        :class="{'is-danger': errors.has('id')}"
        v-validate="'required|alpha_num|min:4'"
        v-model="user.id"
        placeholder="아이디"
        maxlength="20"
      >
      <span v-show="errors.has('id')" class="help is-danger">{{ errors.first('id') }}</span>
    </p>
    <p>
      <label>비밀번호</label>
      <input
        class="full"
        type="password"
        name="pwd"
        id="pwd"
        :class="{'is-danger': errors.has('pwd')}"
        v-validate="'required|alpha_num|min:4'"
        v-model="user.pwd"
        placeholder="비밀번호"
        maxlength="20"
        ref="pwd"
      >
      <span v-show="errors.has('pwd')" class="help is-danger">{{ errors.first('pwd') }}</span>
    </p>
    <p>
      <button class="full" type="button" v-on:click="login">로그인</button>
    </p>
    <p v-on:click="mvSignin">회원가입</p>
  </section>
</template>

<script>
  module.exports = {
    name: "login",
    data() {
      return {
        user: {
          id: "",
          pwd: ""
        }
      };
    },

    created() {
      if ($app.user.savedId) {
        this.user.id = $app.user.savedId;
      }
    },

    methods: {
      mvSignin: function() {
        router.push("/signin");
      },
      login: function() {
        let $this = this;

        if (
          $this.user.id == "" ||
          $this.user.pwd == "" ||
          $this.errors.has("id") ||
          $this.errors.has("pwd")
        ) {
          return alert("올바른 값을 입력해 주세요.");
        }

        axios
          .post("/api/user", $this.user, {
            headers: { "Content-Type": "application/json" }
          })
          .then(response => {
            if (response.data.code == "0000") {
              $app.user.id = response.data.data.id;
              router.push({ path: "/search" });
            } else if (response.data.code == "0101") {
              $this.user = {};
              return alert("아이디 또는 비밀번호가 일치하지 않습니다.");
            } else if (response.data.code == "1111") {
              return alert("오류가 발생하였습니다.");
            }
          })
          .catch(err => {
            return alert("오류가 발생하였습니다.");
          });
      }
    }
  };
</script>

<style scoped>
  #loginWrap {
    width: 400px;
    margin: 0 auto;
  }
  #loginWrap p {
    position: relative;
    margin-bottom: 20px;
  }
  #loginWrap p:nth-of-type(4) {
    font-weight: bold;
    float: right;
    text-decoration: underline;
    cursor: pointer;
    padding: 20px;
  }
  #loginWrap p:nth-of-type(4):hover {
    color: green;
  }

  #loginWrap .is-danger {
    color: red;
    margin-top: 5px;
    font-size: 12px;
  }
</style>