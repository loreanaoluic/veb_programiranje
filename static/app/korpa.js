Vue.component("korpa", {
    data: function () {
        return {
            items: [],
            manifestacije: {},
            ukupnaCena: 0,
            ukupnaCenaSaPopustom: 0
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
                <td>
                    <span>
                        <button class="korpa" v-on:click="obrisi(item)">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="red" class="bi bi-trash" viewBox="0 0 16 16">
                              <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                              <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                            </svg>
                        </button>
                    </span>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <br><br>
      <i style="float:right" class="material-icons text-info mr-2">Ukupna cena: {{ ukupnaCena }} din </i><br>
      <p style="float:right" class="material-icons text-info mr-2"><b>CENA SA POPUSTOM: {{ ukupnaCenaSaPopustom }} din </b></p><br><br>
      <div class="modal-footer" style="float:right">
        <button type="button" class="btn btn-danger"v-on:click="odustani()">Odustani</button>
        <button type="button" class="btn btn-info"v-on:click="rezervisi()">Potvrdi rezervaciju</button>
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
                axios.post('/karte/cenaSaPopustom?cena=' + data.ukupnaCena)
                    .then(function (response) {
                        data.ukupnaCenaSaPopustom = response.data;
                    })
            })
    }
    ,
    methods : {
        init : function() {
        },
        rezervisi : function () {
            axios.post('/karte/rezervisi')
                .then(function (response) {
                    localStorage.setItem('korisnik', JSON.stringify(response.data))
                    window.location.href = "#/karte-kupca";
                    window.location.reload();
                })
        },
        odustani : function () {
            axios.post('/karte/odustani')
                .then(function (response) {
                    window.location.reload();
                })
        },
        obrisi : function (item) {
            axios.post('/karte/obrisi/' + item.id)
                .then(function (response) {
                    window.location.reload();
                })
        }
    }

});
