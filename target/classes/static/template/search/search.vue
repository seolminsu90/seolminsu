<template>
  <section id="searchWrap">
    <input
      type="text"
      class="full"
      v-on:keyup="enter"
      placeholder="검색어 입력"
      v-model="searchObj.query"
    >
    <button id="searchBtn" type="button" v-on:click="search">검색</button>
    <section>
      <article id="searchList" v-if="!detailShow">
        <ul>
          <template v-if="searchResult.documents != null && searchResult.documents.length > 0">
            <li class="description">
              <span>[검색 결과 {{pagination.docCount}}건]</span>클릭하면 상세정보를 볼 수 있습니다.
            </li>
            <li
              class="result"
              v-for="(doc, idx) in searchResult.documents"
              v-on:click="detail(idx)"
              v-bind:key="idx"
            >
              <img v-bind:title="doc.title" v-bind:src="((doc.thumbnail=='')?'/image/noimage.png':doc.thumbnail)" >
            </li>
          </template>
          <template v-else>
            <li class="no-result">
              검색 결과 없음
              <br>검색어를 입력해 주세요.
            </li>
          </template>
        </ul>
        <ol id="handmade-pagination" v-if="pagination.show">
          <li v-on:click="searchPage(-1)">이전</li>
          <template v-for="i in pagination.endPage">
            <li v-if="i >= pagination.startPage" v-bind:class="{ on : i==pagination.currentPage}" v-on:click="search(null, i)" >{{i}}</li>
          </template>
          <li v-on:click="searchPage(1)">다음</li>
        </ol>
      </article>
      <article id="search" v-if="detailShow">
        <!--  상세 정보에는 제목, 도서 썸네일, 소개, ISBN, 저자, 출판사, 출판일, 정가, 판매가 -->
        <div id="detail">
          <div id="detail-left">
            <img v-bind:title="searchDetail.title" v-bind:src="((searchDetail.thumbnail=='')?'/image/noimage.png':searchDetail.thumbnail)">
          </div>
          <div id="detail-right">
            <table>
              <tbody>
                <tr>
                  <td>제목</td>
                  <td>{{searchDetail.title}}</td>
                </tr>
                <tr>
                  <td>설명</td>
                  <td>{{searchDetail.contents}}</td>
                </tr>
                <tr>
                  <td>ISBN</td>
                  <td>{{searchDetail.isbn}}</td>
                </tr>
                <tr>
                  <td>저자</td>
                  <td>{{searchDetail.authors}}</td>
                </tr>
                <tr>
                  <td>출판사</td>
                  <td>{{searchDetail.publisher}}</td>
                </tr>
                <tr>
                  <td>출판일</td>
                  <td>{{searchDetail.datetime | momentDate}}</td>
                </tr>
                <tr>
                  <td>정가</td>
                  <td>
                    <strong>{{searchDetail.price}}</strong>원
                  </td>
                </tr>
                <tr>
                  <td>판매가</td>
                  <td>{{searchDetail.sale_price}}원</td>
                </tr>
              </tbody>
            </table>
          </div>
          <button class="full" type="button" v-on:click="closeDetail">상세보기 닫기</button>
        </div>
      </article>
      <article id="rank">
        <div>
          <p>인기 키워드</p>
          <table>
            <thead>
              <tr>
                <th>키워드</th>
                <th>검색횟수</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="keywordList.length > 0">
                <tr v-for="(obj, idx) in keywordList" v-bind:key="idx">
                  <td>{{obj.keyword}}</td>
                  <td>{{obj.searchCount}}</td>
                </tr>
              </template>
              <template v-else>
                <tr>
                  <td colspan="2">검색 기록이 없습니다.</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
        <div>
          <p>내 검색 히스토리</p>
          <table>
            <thead>
              <tr>
                <th>키워드</th>
                <th>검색일시</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="historyList.length > 0">
                <tr v-for="(obj,idx) in historyList" v-bind:key="idx">
                  <td>{{obj.keyword}}</td>
                  <td>{{obj.regdate | moment}}</td>
                </tr>
              </template>
              <template v-else>
                <tr>
                  <td colspan="2">검색 기록이 없습니다.</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </article>
    </section>
  </section>
</template>

<script>
  module.exports = {
    name: "search",
    data() {
      return {
        detailShow: false,
        pagination: {
          show: false,
          startPage: 0,
          endPage: 0,
          currentPage: 0,
          docCount: 0,
          totalPage: 0
        },
        keywordList: [],
        historyList: [],
        searchObj: {
          query: "",
          page: 1
        },
        searchResult: {},
        searchDetail: {}
      };
    },
    created() {
      this.init();
    },
    filters: {
      moment: function(date) {
        return moment(date).format("YYYY/MM/DD HH:mm:ss");
      },
      momentDate: function(date) {
        return moment(date).format("YYYY년MM월DD일");
      }
    },
    methods: {
      init: function() {
        this.getPopularKeyword();
        this.getSearchHistory();
      },

      pagingInit: function(currentPage, docCount) {
        let $this = this;
        let docCountOnePage = 10;
        let pageGroupSize = 10;
        let containPageGroup = Math.ceil(currentPage / pageGroupSize);
        let totalPage = Math.ceil(docCount / docCountOnePage);
        let startPage = (containPageGroup - 1) * pageGroupSize + 1;
        let endPage =
          startPage + pageGroupSize - 1 > totalPage
            ? totalPage
            : startPage + pageGroupSize - 1;

        let paginationObj = {
          show: true,
          currentPage: currentPage,
          docCount: docCount,
          startPage: startPage,
          endPage: endPage,
          totalPage: totalPage
        };
        $this.$set($this, "pagination", paginationObj);
      },

      getPopularKeyword: function() {
        let $this = this;
        axios
          .get("/api/keyword")
          .then(response => {
            if (response.data.code == "0101" || response.data.code == "0300") {
              router.push("/");
            } else if (response.data.code == "0000") {
              $this.$set($this, "keywordList", response.data.data);
            }
          })
          .catch(err => {
            return alert("오류가 발생하였습니다.");
          });
      },

      getSearchHistory: function() {
        let $this = this;
        axios
          .get("/api/history")
          .then(response => {
            if (response.data.code == "0101" || response.data.code == "0300") {
              router.push("/");
            } else if (response.data.code == "0000") {
              $this.$set($this, "historyList", response.data.data);
            }
          })
          .catch(err => {
            return alert("오류가 발생하였습니다.");
          });
      },

      enter: function(e) {
        if (e.keyCode == 13) {
          this.search();
        }
      },
      searchPage: function(type) {
        let $this = this;
        let currentPage = $this.pagination.currentPage;
        if (type == -1) {
          if (currentPage - 10 <= 0) {
            return alert("처음 페이지입니다.");
          } else {
            currentPage-=10;
          }
        } else if (type == 1) {
          if(currentPage == $this.pagination.totalPage){
            return alert("끝 페이지입니다.");
          } else if (currentPage + 10 > $this.pagination.totalPage) {
            currentPage = $this.pagination.totalPage;
          } else {
            currentPage+=10;
          }
        }
        $this.search(null, currentPage);
      },
      search: function(e, currentPage) {
        let $this = this;
        $this.closeDetail();
        $this.searchObj.page = 1;

        if ($this.searchObj.query == "") {
          return alert("검색어를 입력해주세요.");
        }

        if (currentPage != undefined) {
          $this.searchObj.page = currentPage;
        }

        axios
          .get("/api/search", {
            params: $this.searchObj
          })
          .then(response => {
            if (response.data.code == "0101" || response.data.code == "0300") {
              router.push("/");
            } else if (response.data.code == "0000") {
              $this.init();

              let resultData = response.data.data;
              if (resultData.documents.length == 0) {
                alert("검색 결과가 없습니다.");
                let paginationObj = {
                  show: false,
                  startPage: 0,
                  endPage: 0,
                  currentPage: 0,
                  docCount: 0,
                  totalPage: 0
                };
                $this.$set($this, "pagination", paginationObj);
              } else {
                $this.pagingInit(
                  $this.searchObj.page,
                  resultData.meta.pageable_count
                );
              }
              $this.$set($this, "searchResult", resultData);
            }
          })
          .catch(err => {
            console.log(err);
            return alert("오류가 발생하였습니다.");
          });
      },

      detail: function(idx) {
        let $this = this;
        $this.detailShow = true;
        $this.searchDetail = $this.searchResult.documents[idx];
      },

      closeDetail: function() {
        let $this = this;
        $this.detailShow = false;
        $this.searchDetail = {};
      }
    }
  };
</script>

<style scoped>
  #searchWrap {
    width: 100%;
    margin: 0 auto;
    position: relative;
  }

  #searchWrap #searchBtn {
    position: absolute;
    z-index: 10;
    right: 0px;
    top: -1px;
    padding: 0 60px;
    font-size: 20px;
    width: 200px;
    max-width: 200px;
    height: 54px;
  }

  #searchWrap input.full {
    width: calc(100% - 210px);
  }

  #search,
  #searchList {
    float: left;
    width: 740px;
    height: auto;
    border-radius: 6px;
    margin-top: 20px;
  }

  #searchList .no-result {
    font-size: 30px;
    font-weight: bold;
    margin: 50px 0;
    line-height: 50px;
    text-align: center;
  }

  #searchList .description {
    padding: 5px;
    font-weight: bold;
    font-size: 12px;
  }

  #searchList .description span {
    color: green;
  }

  #searchList .result {
    display: inline-block;
    width: 20%;
  }

  #searchList .result img {
    width: calc(100% - 10px);
    margin: 5px;
    box-shadow: 2px 2px 3px #aaa;
    cursor: pointer;
  }

  #rank {
    float: right;
    width: 450px;
    height: auto;
    padding: 20px 0;
  }

  #rank p {
    font-weight: bold;
    font-size: 16px;
  }

  #rank div {
    border-radius: 6px;
    background: #fffaea;
    min-height: 200px;
    height: auto;
    margin-bottom: 10px;
    padding: 10px;
    margin-bottom: 10px;
    box-shadow: 2px 2px 3px #aaa;
  }

  #rank div:nth-child(even) {
    background: #edf5ff;
  }

  #rank table {
    margin-top: 10px;
    font-size: 14px;
    width: 100%;
  }

  #rank table tbody tr {
    font-size: 11px;
    height: 20px;
    line-height: 20px;
  }

  #rank table th {
    text-align: left;
    padding-bottom: 5px;
    border-bottom: 2px solid #3c1e1e;
  }

  #rank table th:nth-of-type(1) {
    width: 65%;
  }

  #detail {
    width: 100%;
    height: 400px;
    padding: 20px 0;
    position: relative;
  }

  #detail #detail-left {
    width: 300px;
    height: 360px;
    float: left;
  }

  #detail #detail-left img {
    width: 100%;
    height: 100%;
  }

  #detail #detail-right {
    width: 400px;
    height: 360px;
    padding: 0 20px;
    float: right;
    font-size: 12px;
  }

  #detail #detail-right strong {
    text-decoration: line-through;
  }

  #detail #detail-right tr {
    height: 30px;
    line-height: 15px;
  }

  #detail #detail-right td:nth-of-type(1) {
    display: inline-block;
    font-size: 13px;
    width: 65px;
    font-weight: bold;
    color: #825c23;
  }

  #detail button {
    position: absolute;
    bottom: 0px;
    left: 0px;
  }

  #handmade-pagination {
    text-align: center;
    width: 100%;
    margin-top: 20px;
  }

  #handmade-pagination li {
    padding: 5px;
    display: inline-block;
    font-size: 14px;
    color: #825c23;
    cursor: pointer;
  }

  #handmade-pagination li.on {
    font-weight: bold;
    color: red;
    font-size: 16px;
  }
</style>