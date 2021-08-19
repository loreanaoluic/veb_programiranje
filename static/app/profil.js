Vue.component("profil", {
    data: function () {
        return {
            korisnik: "",
            datumRodjenjaFormat: "",
            datumIVremeOdrzavanja: "",
            tipKupca: "",
            karte: [],
            input: {
                datumRodjenja: ""
            }
        }
    },
    template: ` 
 
<div class="container">
    <div class="main-body">  
          <div class="row gutters-sm">
            <div class="col-md-4 mb-3">
              <div class="card">
                <div class="card-body">
                  <div class="d-flex flex-column align-items-center text-center">
                    <div v-if="(korisnik.pol === 'MUSKO')">
                        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
                    </div>
                    <div v-if="(korisnik.pol === 'ZENSKO')">
                        <img src="https://bootdey.com/img/Content/avatar/avatar8.png" alt="Admin" class="rounded-circle" width="150">
                    </div>
                    <div class="mt-3">
                        <h6 class="d-flex align-items-center mb-3"> <b>{{ korisnik.korisnickoIme }} </b></h6>
                        <i class="material-icons text-info mr-2"> {{ korisnik.uloga }} </i>
                    </div>
                  </div>
                </div>
              </div>
              <div v-if="(korisnik.uloga === 'KUPAC')">
                <div class="card mt-3">
                    <div class="mt-3" align="center">
                        <h6 class="mb-0">Broj bodova: <i>{{ korisnik.brojBodova }}</i></h6>
                        <br>
                        <h6 class="mb-0">Rang: <i>{{ tipKupca.nazivTipa }}</i></h6>
                        <br>
                    </div>                
                </div>
              </div>
            </div>
            <div class="col-md-8">
              <div class="card mb-3">
                <div class="card-body">
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Ime</h6>
                    </div>
                    <div class="col-sm-9 text-secondary">
                      {{ korisnik.ime }}
                    </div>
                  </div>
                  <hr>
                     <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Prezime</h6>
                    </div>
                    <div class="col-sm-9 text-secondary">
                      {{ korisnik.prezime }}
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Pol</h6>
                    </div>
                    <div class="col-sm-9 text-secondary">
                      {{ korisnik.pol }}
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Datum rođenja</h6>
                    </div>
                    <div class="col-sm-9 text-secondary">
                      {{ datumRodjenjaFormat }}
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-12">
                      <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#IzmeniModal">Izmeni</button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row gutters-sm">
              
                <div class="col-sm-5 mb-3" v-if="(korisnik.uloga !== 'ADMIN')" v-for="karta in karte" :key="karta.id">
                  <div class="karte">
                      <div class="card-body">
                          <div align="center">
                              <h6><i class="material-icons text-info mr-2"><br><br><b> {{ karta.datumIVremeManifestacije.date.day }}.{{ karta.datumIVremeManifestacije.date.month }}.{{ karta.datumIVremeManifestacije.date.year }} u 
                              {{ karta.datumIVremeManifestacije.time.hour}}:{{ karta.datumIVremeManifestacije.time.minute}} </b></i></h6>
                              <small><b>Cena:</b> {{karta.cena}} din</small>
                              <br>
                              <small><b>Tip karte:</b> {{karta.tipKarte}}</small>
                              <br>
                              <br>
                          </div>
                      </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
         <div class="modal fade" id="IzmeniModal" tabindex="-1" aria-labelledby="IzmeniModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="IzmeniModal">Izmeni lične podatke</h5>
              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="modal-body text-start">
                <form>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.ime" placeholder="Ime">
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.prezime" placeholder="Prezime">
                  </div>
                  <div class="form-group">
                    <div class="form-control">
                        <label>Pol</label>
                        <select name="pol" v-model="input.pol" class="labele">
                          <option value="MUSKO">MUŠKO</option>
                          <option value="ZENSKO">ŽENSKO</option>
                          <option value="DRUGO">DRUGO</option>
                        </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="form-control">
                        <label>Datum rođenja</label>
                        <input type="date" name="datumRodjenja" v-model="input.datumRodjenja" class="labele">
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="izmeni">Potvrdi</button>
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
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
        this.input.ime = this.korisnik.ime;
        this.input.prezime = this.korisnik.prezime;
        this.input.datumRodjenja = moment(this.korisnik.datumRodjenja).format("YYYY-MM-DD");
        this.input.pol = this.korisnik.pol;
        this.input.korisnickoIme = this.korisnik.korisnickoIme;
        this.datumRodjenjaFormat = moment(this.korisnik.datumRodjenja).format("DD.MM.YYYY.")
        axios.get('/korisnici/tipKupca', this.data)
            .then(function (response) {
                data.tipKupca = response.data;
            })
        axios.get('/korisnici/karte', this.data)
            .then(function (response) {
                data.karte = response.data;
            })
    },
    methods : {
        init : function() {
        },
        izmeni(event) {
            axios.post('/korisnici/izmena', this.input)
                .then(function (response) {
                    localStorage.setItem('korisnik', JSON.stringify(response.data))
                    window.location.reload();
                })
                .catch(function (error) {
                    alert(error.response.data);
                });
        }
    }

});