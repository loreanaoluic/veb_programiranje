Vue.component("pregled-svih-korisnika", {
    data: function () {
        return {
            korisnici: [],
            searchParams: {
                ime: "",
                prezime: "",
                korisnickoIme: "",
                kriterijumSortiranja: "IME",
                uloga: "SVE",
                opadajuce: false,
                sumnjivi: false
            },
            ulogovan: {}
        }
    },
    template: ` 
 <div class="container">
   <button class="openbtn" v-on:click="openNav()">&#9776; Filter</button>
    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Ime i prezime</span></th>
                <th><span>Uloga</span></th>
                <th><span>Korisničko ime</span></th>
                <th><span>Pol</span></th>
                <th><span>Datum rođenja</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="korisnik in korisnici" :key="korisnik.korisnickoIme">
                <td><span>{{ korisnik.ime }} {{ korisnik.prezime }}</span></td>
                <td><span>{{ korisnik.uloga }}</span></td>
                <td><span>{{ korisnik.korisnickoIme }}</span></td>
                <td><span>{{ korisnik.pol }}</span></td>
                <td><span>{{ korisnik.datumRodjenja.day }}.{{ korisnik.datumRodjenja.month }}.{{ korisnik.datumRodjenja.year }}</span></td>
                <td><span><div v-if="(korisnik.uloga === 'KUPAC')">
                      <div v-if="(korisnik.sumnjiv === true)">
                        <i style="color:#808080">SUMNJIV</i>
                      </div>
                </div></span></td>
                 <div class="modal-footer" v-if="(korisnik.uloga !== 'ADMIN')">
                  <div v-if="(korisnik.obrisan !== true)">
                    <button style="font-size: 12px" class="btn btn-danger" v-on:click="obrisi(korisnik)">Obriši</button>
                  </div>
                  <div v-if="(korisnik.blokiran !== true)">
                    <button style="font-size: 12px" class="btn btn-outline-danger" v-on:click="blokiraj(korisnik)">Blokiraj</button>
                  </div>
                  <div v-if="(korisnik.blokiran === true)">
                    <button style="font-size: 12px" class="btn btn-outline-primary" v-on:click="odblokiraj(korisnik)">Odblokiraj</button>
                  </div>
                  </div>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" v-on:click="closeNav()">&times;</a>
		  <table>
			  <tr><td style="color:white">Ime:</td> <td><input type="text" name="ime" v-model="searchParams.ime" /></td></tr><br>
              <tr><td style="color:white">Prezime:</td> <td><input type="text" name="prezime" v-model="searchParams.prezime" /></td></tr><br>
			  <tr><td style="color:white">Korisničko ime:</td> <td><input type="text" name="korisnickoIme" v-model="searchParams.korisnickoIme" /></td></tr><br>

			  <tr><td style="color:white">Sortiraj po:</td>
			  <td>
			  <select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
			  <option value="IME">IME</option>
			  <option value="PREZIME">PREZIME</option>
			  <option value="KORISNICKO_IME">KORISNIČKO IME</option>
              <option value="BROJ_BODOVA">BROJ BODOVA</option>
			</select>
			</td></tr><br>
		    <tr><td style="color:white">Uloga:</td> 
			  <td>
			  <select name="uloga" id="uloga" v-model="searchParams.uloga" >
			  <option value="ADMIN">ADMIN</option>
			  <option value="PRODAVAC">PRODAVAC</option>
			  <option value="KUPAC">KUPAC</option>
			  <option value="SVE">SVE</option>
			</select>
			</td></tr><br>
			  <tr><td style="color:white">Opadajući prikaz:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr><br>
              <tr><td style="color:white">Prikaz sumnjivih kupaca:</td> <td><input type="checkbox" name="sumnjivi" v-model="searchParams.sumnjivi" ></td></tr>
			  <br><br><br>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Potvrdi" v-on:click="pretrazi()" /></td></tr>
		  </table>
	</div>
</div>

`
    ,
    mounted(){
        this.ulogovan = JSON.parse(localStorage.getItem('korisnik'))
        let data = this;
        axios.get('/korisnici/svi-korisnici', this.data)
            .then(function (response) {
                data.korisnici = response.data;
            })
    },
    methods : {
        init : function() {
        },
        pretrazi : function () {
            let self = this;
            axios.post('/korisnici/pretraga?ime='  + this.searchParams.ime + "&prezime=" + this.searchParams.prezime +
                "&korisnickoIme=" + this.searchParams.korisnickoIme + "&kriterijumSortiranja=" +
                this.searchParams.kriterijumSortiranja  + "&uloga=" + this.searchParams.uloga + "&opadajuce="
                + this.searchParams.opadajuce + "&sumnjivi=" + this.searchParams.sumnjivi)
                .then(function (response) {
                    self.korisnici = response.data;

                })
                .catch(function (error) {
                    alert(error.response.data);
                });
        },
        openNav : function () {
            document.getElementById("mySidebar").style.width = "330px";
        },

        closeNav : function () {
            document.getElementById("mySidebar").style.width = "0";
        },
        obrisi : function(korisnik) {
            axios.post('/korisnici/obrisi/' + korisnik.korisnickoIme)
            window.location.href = "#/pregled-svih-korisnika";
            window.location.reload();
        },
        blokiraj : function(korisnik) {
            axios.post('/korisnici/blokiraj/' + korisnik.korisnickoIme)
            window.location.href = "#/pregled-svih-korisnika";
            window.location.reload();
        },
        odblokiraj : function(korisnik) {
            axios.post('/korisnici/odblokiraj/' + korisnik.korisnickoIme)
            window.location.href = "#/pregled-svih-korisnika";
            window.location.reload();
        }
    }

});
