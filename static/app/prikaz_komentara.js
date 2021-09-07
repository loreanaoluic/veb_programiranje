Vue.component("prikaz-komentara", {
    data: function () {
        return {
            komentari: [],
            korisnik: {},
            manifestacija: {},
            input: {
                tekstKomentara: "",
                ocena: 0
            }
        }
    },
    template: ` 
 <div class="container">
    <br><h3 class="material-icons text-info mr-2" style="text-align: center">{{ manifestacija.naziv }}</h3><br>
        <div v-if="(korisnik.uloga === 'KUPAC')">
                <div class="rate">
                    <input type="radio" id="star5" name="rate" value="5" />
                    <label for="star5" title="text">5 stars</label>
                    <input type="radio" id="star4" name="rate" value="4" />
                    <label for="star4" title="text">4 stars</label>
                    <input type="radio" id="star3" name="rate" value="3" />
                    <label for="star3" title="text">3 stars</label>
                    <input type="radio" id="star2" name="rate" value="2" />
                    <label for="star2" title="text">2 stars</label>
                    <input type="radio" id="star1" name="rate" value="1" />
                    <label for="star1" title="text">1 star</label>
                </div>
                <br>
                <div class="form-group" style="text-align:center">
                    <input type="text" class="form-control" v-model="input.tekstKomentara" placeholder="Vaš komentar"><br>
                    <button class="btn btn-info" v-on:click="posalji()">Pošaljite komentar i ocenu!</button><br>
                </div>
        </div>
    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Tekst komentara</span></th>
                <th><span>Ocena</span></th>
                <th><span>Kupac</span></th>
                <th><span>Status komentara</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="komentar in komentari" :key="komentar.id">
                <td><span>{{ komentar.tekstKomentara }}</span></td>
                <td><span>{{ komentar.ocena }}</span></td>
                <td><span>{{ komentar.kupac }}</span></td>
                <td><span>{{ komentar.status }}</span></td>
                <div v-if="(korisnik.uloga === 'PRODAVAC')">
                    <td><span><div v-if="(komentar.status === 'NA_CEKANJU')">
                        <button style="font-size: 12px" class="btn btn-primary" v-on:click="prihvati(komentar)">Prihvati</button>
                    </div></span></td>
                    <td><span><div v-if="(komentar.status === 'NA_CEKANJU')">
                        <button style="font-size: 12px" class="btn btn-danger" v-on:click="odbij(komentar)">Odbij</button>
                    </div></span></td>
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
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'));
        this.manifestacija = JSON.parse(localStorage.getItem('manifestacija'));
        let data = this;
        axios.get('/komentari/' + this.manifestacija.id, this.data)
            .then(function (response) {
                data.komentari = response.data;
            })
    },
    methods : {
        init : function() {
        },
        prihvati : function (komentar) {
            axios.post('/komentari/prihvati/' + komentar.id)
            window.location.reload();
        },
        odbij : function (komentar) {
            axios.post('/komentari/odbij/' + komentar.id)
            window.location.reload();
        },
        posalji : function () {
            let radios = document.getElementsByName('rate');
            for (let i = 0, length = radios.length; i < length; i++) {
                if (radios[i].checked) {
                    this.input.ocena = radios[i].value;
                    break;
                }
            }
            axios.post('/komentari/posalji/' + this.manifestacija.id, this.input)
            window.location.reload();
        }
    }

});
