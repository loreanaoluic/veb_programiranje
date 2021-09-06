Vue.component("manifestacija", {
    data: function () {
        return {
            manifestacija: {},
            aktivna: "",
            korisnik: {},
            input: {
                geografskaSirina: 45.2673,
                geografskaDuzina: 19.8339
            },
            lokacija: {},
            poster: "",
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
                <button type="button" class="btn btn-info" v-on:click="izmeniManifestaciju()">Izmeni</button>
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
                localStorage.setItem('lokacija', JSON.stringify(response.data));
            })
        if (this.manifestacija.status === true) {
            this.aktivna = "AKTIVNA";
        } else {
            this.aktivna = "NEAKTIVNA";
        }
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
        izmeniManifestaciju : function() {
            window.location.href = "#/izmena-manifestacije";
            window.location.reload();
        },
        rezervisi : function() {
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
