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
                    </div>
                    <div v-if="(korisnik === null)" class="navbar-nav my-2 my-lg-0">
                        <router-link to="/prijava" class="nav-link">Prijavi se</router-link>
                        <router-link to="/registracija" class="nav-link">Registruj se</router-link>
                    </div>
                    <div v-if="(korisnik !== null)" class="navbar-nav my-2 my-lg-0">
                        <router-link to="/profil" class="nav-link">Profil</router-link>
                        <router-link to="/odjava" class="nav-link">Odjavi se</router-link>
                    </div>
                </div>
            </div>
        </nav>
    `,
    mounted(){
        this.korisnik = JSON.parse(localStorage.getItem('korisnik'))
    }
})