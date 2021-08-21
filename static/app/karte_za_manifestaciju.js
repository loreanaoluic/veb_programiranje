Vue.component("karte-za-manifestaciju", {
    data: function () {
        return {
            karte: [],
            manifestacija: {}
        }
    },
    template: ` 
<div class="container">
    <div class="main-body">
          <h4 class="material-icons text-info mr-2"> {{ manifestacija.naziv }} </h4>  
          <i> <p>Datum i vreme održavanja: {{ manifestacija.datumIVremeOdrzavanja.date.day }}.{{ manifestacija.datumIVremeOdrzavanja.date.month }}.{{ manifestacija.datumIVremeOdrzavanja.date.year }}. u 
                              {{ manifestacija.datumIVremeOdrzavanja.time.hour}}:{{ manifestacija.datumIVremeOdrzavanja.time.minute}} </p></i>
          <div class="row gutters-sm">
            <div class="col-sm-5 mb-3" v-for="karta in karte" :key="karta.id">
                <div class="card">
                    <div class="card-body">
                          <div class="d-flex flex-column align-items-center text-center">
                          <p class="d-flex align-items-center mb-3"> Kupac: {{ karta.kupac }} </p>
                          <i> Status: {{ karta.statusKarte }}</i>
                          <p> Tip karte: {{ karta.tipKarte }}</p>
                          <p> Cena: {{ karta.cena }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
`
    ,
    mounted(){
        let data = this;
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        axios.get('/karte/' + this.manifestacija.id)
            .then(function (response) {
                data.karte = response.data;
            })
    },
    methods : {
        init : function() {
        }
    }

});
