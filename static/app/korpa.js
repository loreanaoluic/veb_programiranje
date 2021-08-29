Vue.component("korpa", {
    data: function () {
        return {
            items: [],
            manifestacije: {},
            ukupnaCena: 0
        }
    },
    template: ` 
  <div class="container">
    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Manifestacija</span></th>
                <th><span>Tip karata</span></th>
                <th><span>Broj karata</span></th>
                <th><span>Cena</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="item in items" :key="item.id">
                <td><span>{{ manifestacije[item.manifestacija].naziv }}</span></td>
                <td><span>{{ item.tipKarte }}</span></td>
                <td><span>{{ item.kolicina }}</span></td>
                <td><span>{{ item.ukupnaCena }} din </span></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <br><br>
      <i style="float:right" class="material-icons text-info mr-2"><b>Ukupna cena: {{ ukupnaCena }} din </b></i><br>
      <div class="modal-footer" style="float:right">
        <button type="button" class="btn btn-info"v-on:click="rezervisi()">Potvrdi rezervaciju!</button>
      </div>
  </div>

`
    ,
    mounted() {
        let data = this;
        axios.post('/karte/korpa')
            .then(function (response) {
                data.items = response.data;
            })
        axios.get('/manifestacijeMapa', this.data)
            .then(function (response) {
                data.manifestacije = response.data;
            })
        axios.post('/karte/cena')
            .then(function (response) {
                data.ukupnaCena = response.data;
            })
    }
    ,
    methods : {
        init : function() {
        },
        rezervisi : function () {
            axios.post('/karte/rezervisi?cena=' + this.ukupnaCena)
                .then(function (response) {
                    window.location.href = "#/karte-kupca";
                    window.location.reload();
                })
        }
    }

});
