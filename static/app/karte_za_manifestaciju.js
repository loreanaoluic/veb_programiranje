Vue.component("karte-za-manifestaciju", {
    data: function () {
        return {
            karte: [],
            manifestacija: {},
            korisnik: {}
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
                <th><span>Kupac</span></th>
                <th><span>Status</span></th>
                <th><span>Tip karte</span></th>
                <th><span>Cena</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="karta in karte" :key="karta.id">
                <td><span>{{ karta.kupac }}</span></td>
                <td><span>{{ karta.statusKarte }}</span></td>
                <td><span>{{ karta.tipKarte }}</span></td>
                <td><span>{{ karta.cena }}</span></td>
                 <div v-if="(korisnik.uloga === 'ADMIN')">
                    <button class="korpa" v-on:click="obrisi(karta)">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="34" fill="red" class="bi bi-trash" viewBox="0 -6 16 16">
                          <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                          <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                        </svg>
                    </button>
                 </div>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
`
    ,
    mounted(){
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
        let data = this;
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        axios.get('/karte/' + this.manifestacija.id)
            .then(function (response) {
                data.karte = response.data;
            })
    },
    methods : {
        init : function() {
        },
        obrisi : function(karta) {
            axios.post('/karte/obrisi-admin/' + karta.id)
            window.location.href = "#/karte-za-manifestaciju";
            window.location.reload();
        }
    }

});
