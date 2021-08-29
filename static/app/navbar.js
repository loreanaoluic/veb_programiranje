Vue.component("navbar",{
    data: function(){
        return{
            korisnik : null
        }
    },

    template:`
        <nav id="navbar" class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarNavAltMarkup"
                    aria-controls="navbarNavAltMarkup" aria-expanded="false"
                    aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                    <div class="navbar-nav mr-auto mt-2 mt-lg-0">
                        <router-link to="/" class="nav-link">Početna strana</router-link>
                        <div v-if="(korisnik !== null)">
                            <div v-if="(korisnik.uloga === 'ADMIN')">
                                <router-link to="/registracija" class="nav-link">Registruj prodavca</router-link>
                            </div>
                        </div>
                         <div v-if="(korisnik !== null)">
                            <div v-if="(korisnik.uloga === 'ADMIN')">
                                <router-link to="/pregled-svih-korisnika" class="nav-link">Pregled korisnika</router-link>
                            </div>
                        </div>
                        <div v-if="(korisnik !== null)">
                            <div v-if="(korisnik.uloga === 'PRODAVAC')">
                                <router-link to="/manifestacije-prodavca" class="nav-link">Moje manifestacije</router-link>
                            </div>
                        </div>
                        <div v-if="(korisnik !== null)">
                            <div v-if="(korisnik.uloga === 'PRODAVAC')">
                                <router-link to="/nova-manifestacija" class="nav-link">Dodaj manifestaciju</router-link>
                            </div>
                        </div>
                         <div v-if="(korisnik !== null)">
                            <div v-if="(korisnik.uloga === 'KUPAC')">
                                <router-link to="/karte-kupca" class="nav-link">Moje karte</router-link>
                            </div>
                        </div>
                    </div>
                    <div v-if="(korisnik === null)" class="navbar-nav my-2 my-lg-0">
                        <router-link to="/prijava" class="nav-link">Prijavi se</router-link>
                        <router-link to="/registracija" class="nav-link">Registruj se</router-link>
                    </div>
                    <div v-if="(korisnik !== null)" class="navbar-nav my-2 my-lg-0">
                        <div v-if="(korisnik.uloga === 'KUPAC')">
                            <button class="korpa" v-on:click="korpa()">
                                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="40" fill="grey" class="bi bi-cart" viewBox="0 0 20 16">
                                    <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                                </svg>
                            </button>
                        </div>
                        <router-link to="/profil" class="nav-link">Profil</router-link>
                        <router-link to="/odjava" class="nav-link">Odjavi se</router-link>
                    </div>
                </div>
            </div>
        </nav>
    `,
    mounted(){
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
    },
    methods : {
        korpa : function () {
            window.location.href = "#/korpa";
        }
    }
})