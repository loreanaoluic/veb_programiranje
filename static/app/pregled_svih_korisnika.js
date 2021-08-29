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
                opadajuce: false
            }
        }
    },
    template: ` 
<div class="container">
    <div class="main-body">  
          <button class="openbtn" v-on:click="openNav()">&#9776; Filter</button>
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
			  <tr><td style="color:white">Opadajući prikaz:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <br><br><br>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Potvrdi" v-on:click="pretrazi()" /></td></tr>
		  </table>
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
        },
        pretrazi : function () {
            let self = this;
            axios.post('/korisnici/pretraga?ime='  + this.searchParams.ime + "&prezime=" + this.searchParams.prezime +
                "&korisnickoIme=" + this.searchParams.korisnickoIme + "&kriterijumSortiranja=" +
                this.searchParams.kriterijumSortiranja  + "&uloga=" + this.searchParams.uloga + "&opadajuce="
                + this.searchParams.opadajuce)
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
        }
    }

});
