Vue.component("manifestacija", {
    data: function () {
        return {
            manifestacija: {},
            aktivna: "",
            korisnik: {},
            input: {},
            lokacija: {},
            poster: "",
            novaLokacija: {},
            rezervacija: {
                tipKarte: "REGULAR"
            }
        }
    },
    template: ` 
 <div class="modal-dialog" xmlns="http://www.w3.org/1999/html">
    <div class="modal-content">
      <div class="modal-body text-start">
        <form>
          <div class="d-flex flex-column align-items-center text-center">
              <img  v-bind:src="manifestacija.poster" alt="Avatar" style="width:100%">
              <h4 class="d-flex align-items-center mb-3"> <b> {{ manifestacija.naziv }} </b></h4>
              <i class="material-icons text-info mr-2">{{ manifestacija.tipManifestacije }} </i>
              <br>
              <p>Datum i vreme održavanja: {{ manifestacija.datumIVremeOdrzavanja.date.day }}.{{ manifestacija.datumIVremeOdrzavanja.date.month }}.{{ manifestacija.datumIVremeOdrzavanja.date.year }}. u 
                  {{ manifestacija.datumIVremeOdrzavanja.time.hour}}:{{ manifestacija.datumIVremeOdrzavanja.time.minute}}</p>
              <p><i>REGULAR karta: {{ manifestacija.cenaRegular }} din</i></p>
              <p><i>Lokacija: {{ lokacija.adresa }} </i></p>
              <p><b>Preostalo {{ manifestacija.brojMesta }} karata! </b></p>
              <p><i>Status: {{ aktivna }} </i></p>
          </div>
        </form>
      </div>
      <div v-if="(korisnik !== null)">
        <div v-if="(korisnik.uloga === 'ADMIN')">
            <div v-if="(aktivna === 'NEAKTIVNA')">
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" v-on:click="aktivno()">Odobri manifestaciju</button>
                </div>
            </div>
        </div>
         <div v-if="(korisnik.uloga === 'PRODAVAC')">
            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#IzmeniModal">Izmeni</button>
            </div>
        </div>
        <div v-if="(korisnik.uloga === 'KUPAC')">
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#RezervisiModal">Rezerviši kartu na vreme!</button>
            </div>
        </div>
         <div v-if="(korisnik.uloga === 'ADMIN')">
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#ObrisiModal">Obriši</button>
            </div>
        </div>
    </div>
    
     <div class="modal fade" id="ObrisiModal" tabindex="-1" aria-labelledby="ObrisiModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="ObrisiModal">Obriši manifestaciju</h5>
              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="modal-body text-start">
                <form>
                  <div class="form-group">
                    <p> Da li ste sigurni da želite da obrišete manifestaciju?</p>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="obrisi">Potvrdi</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    
    <div class="modal fade" id="IzmeniModal" tabindex="-1" aria-labelledby="IzmeniModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="IzmeniModal">Izmeni manifestaciju</h5>
              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="modal-body text-start">
                <form>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.naziv" placeholder="Naziv">
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.tipManifestacije" placeholder="Tip manifestacije">
                  </div>
                  <div class="form-group">
                    <input type="number" class="form-control" v-model="input.brojMesta" placeholder="Broj mesta">
                  </div>
                  <div class="form-group">
                    <div class="form-control">
                        <label>Datum i vreme</label>
                        <input type="datetime-local" name="datumIVremeOdrzavanja" v-model="input.datumIVremeOdrzavanja" class="labele">
                    </div>
                  </div>
                   <div class="form-group">
                    <input type="number" class="form-control" v-model="input.cenaRegular" placeholder="Cena regular karte">
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="novaLokacija.adresa" placeholder="Adresa">
                  </div>
                   <div class="form-group">
                    <div class="form-control">
                        <label>Poster</label>
                        <input type="file" class="labele" id="poster" name="poster" multiple v-on:change="posterDodat()">
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="potvrdi">Potvrdi</button>
              </div>
            </div>
          </div>
        </div>
      </div>
       <div class="modal fade" id="RezervisiModal" tabindex="-1" aria-labelledby="RezervisiModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="IzmeniModal">Rezerviši kartu/e</h5>
              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
               <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="modal-body text-start">
                <form>
                  <div class="form-group">
                    <input type="number" class="form-control" v-model="rezervacija.kolicina" placeholder="Broj karata">
                  </div>
				  <div class="form-group">
					<div class="form-control">
						<label>Tip karte</label>
						<select name="tipKarte" v-model="rezervacija.tipKarte" class="labele">
						  <option value="REGULAR">REGULAR</option>
						  <option value="FAN_PIT">FAN PIT</option>
						  <option value="VIP">VIP</option>
						</select>
					</div>
				  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="rezervisi">Rezerviši</button>
              </div>
            </div>
          </div>
        </div>
      </div>
  </div>
  </div>
</div>	  

`
    ,
    mounted() {
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
        let data = this;
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'))
        axios.get('/manifestacije/' + this.manifestacija.id, this.data)
            .then(function (response) {
                data.lokacija = response.data;
                data.novaLokacija.adresa = data.lokacija.adresa;
                data.novaLokacija.geografskaSirina = data.lokacija.geografskaSirina;
                data.novaLokacija.geografskaDuzina = data.lokacija.geografskaDuzina;
            })
        if (this.manifestacija.status === true) {
            this.aktivna = "AKTIVNA";
        } else {
            this.aktivna = "NEAKTIVNA";
        }
        this.input.naziv = this.manifestacija.naziv;
        this.input.tipManifestacije = this.manifestacija.tipManifestacije;
        this.input.cenaRegular = this.manifestacija.cenaRegular;
        this.input.poster = this.manifestacija.poster;
    }
    ,
    methods : {
        aktivno : function() {
            this.aktivna = "AKTIVNA";
            let data = this;
            axios.post('/manifestacije/status/' + this.manifestacija.id, this.data)
                .then(function (response) {
                    data.manifestacija = response.data;
                })
        },
        posterDodat : function() {
            var name = document.getElementById('poster');
            this.input.poster = "images/manifestacije/" + name.files.item(0).name;
        },
        potvrdi : function() {
            let data = this;
            console.log(this.input.datumIVremeOdrzavanja);
            if (this.input.naziv === "" || this.input.tipManifestacije === "" || this.input.brojMesta === "" || this.input.brojMesta === undefined
                || this.input.datumIVremeOdrzavanja === undefined || this.input.cenaRegular === "" || this.input.adresa === ""
                || this.input.geografskaSirina === "" || this.input.geografskaDuzina === "" || this.input.poster === "") {
                alert("Ispunite ispravno sva polja!");
            } else {
                axios.post('/manifestacije/izmenaManifestacije/' + this.manifestacija.id, this.input)
                    .then(function (response) {
                        data.manifestacija = response.data;
                        window.location.href = "#/";
                        window.location.reload();
                    })
            }
        },
        rezervisi : function() {
            let data = this;
            if (this.rezervacija.kolicina === "" || this.rezervacija.kolicina === "0" || this.rezervacija.tipKarte === "") {
                alert("Ispunite ispravno sva polja!");
            } else {
                axios.post('/karte/rezervisi/' + this.manifestacija.id, this.rezervacija)
                    .then(function (response) {
                        if (response.data === "Done") {
                            window.location.href = "#/korpa";
                            window.location.reload();
                        } else {
                            alert("Manifestacija nije aktivna/na stanju ne postoji željeni broj karata!")
                        }
                    })
            }
        },
        obrisi : function() {
            axios.post('/manifestacije/obrisi/' + this.manifestacija.id)
            window.location.href = "#/";
            window.location.reload();
        }
    }

});
