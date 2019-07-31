<template>
  <section id="signinWrap">
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
      <label>비밀번호 확인</label>
      <input
        class="full"
        type="password"
        name="pwd-confirm"
        id="pwd-confirm"
        :class="{'is-danger': errors.has('pwd-confirm')}"
        v-validate="'required|alpha_num|confirmed:pwd|min:4'"
        placeholder="비밀번호 확인"
        maxlength="20"
        data-vv-as="pwd"
      >
      <span
        v-show="errors.has('pwd-confirm')"
        class="help is-danger"
      >{{ errors.first('pwd-confirm') }}</span>
    </p>
    <p>
      <button class="full" type="button" v-on:click="signin">회원가입</button>
    </p>
    <p v-on:click="mvLogin">로그인</p>
  </section>
</template>

<script>
  module.exports = {
    name: "signin",
    data() {
      return {
        user: {
          id: "",
          pwd: ""
        }
      };
    },
    methods: {
      mvLogin: function() {
        router.push("/");
      },
      signin: function() {
        let $this = this;

        if (
          $this.user.id == "" ||
          $this.user.pwd == "" ||
          $this.errors.has("id") ||
          $this.errors.has("pwd") ||
          $this.errors.has("pwd-confirm")
        ) {
          return alert("올바른 값을 입력해 주세요.");
        }

        axios
          .post("/api/user", $this.user, {
            headers: { "Content-Type": "application/json" }
          })
          .then(response => {
            if (response.data.code == "0000") {
              alert("가입이 성공했습니다.");
              $app.user.savedId = response.data.data.id;
              router.push("/");
            } else if (response.data.code == "0100") {
              return alert("이미 가입된 아이디입니다.");
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
  #signinWrap {
    width: 400px;
    margin: 0 auto;
  }
  #signinWrap p {
    position: relative;
    margin-bottom: 20px;
  }
  #signinWrap p:nth-of-type(5) {
    font-weight: bold;
    float: right;
    text-decoration: underline;
    cursor: pointer;
    padding: 20px;
  }
  #signinWrap p:nth-of-type(5):hover {
    color: green;
  }
  #signinWrap .is-danger {
    color: red;
    margin-top: 5px;
    font-size: 12px;
  }
</style>
