Vue.component("pregled-svih-korisnika", {
    data: function () {
        return {
            korisnici: []
        }
    },
    template: ` 
<div class="container">
    <div class="main-body">  
          <div class="row gutters-sm">
            <div class="col-sm-5 mb-3" v-for="korisnik in korisnici" :key="korisnik.korisnickoIme">
                <div class="card">
                    <div class="card-body">
                          <div class="d-flex flex-column align-items-center text-center">
                          <h4 class="d-flex align-items-center mb-3"> <b> {{ korisnik.ime }} {{ korisnik.prezime }} </b></h4>
                          <i class="material-icons text-info mr-2">{{ korisnik.uloga }} </i>
                          <br>
                          <p><b>Korisničko ime: {{ korisnik.korisnickoIme }}</b></p>
                          <p><i>Pol: {{ korisnik.pol }} </i></p>
                          <p>Datum rođenja: {{ korisnik.datumRodjenja.day }}.{{ korisnik.datumRodjenja.month }}.{{ korisnik.datumRodjenja.year }}</p>
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
        axios.get('/korisnici/svi-korisnici', this.data)
            .then(function (response) {
                data.korisnici = response.data;
            })
    },
    methods : {
        init : function() {
        }
    }

});
