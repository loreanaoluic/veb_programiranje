const ManifestacijePocetna = { template: '<pocetna-strana></pocetna-strana>' }
const Registracija = { template: '<registracija></registracija>' }
const Prijava = { template: '<prijava></prijava>' }
const Odjava = { template: '<odjava></odjava>' }
const Profil = { template: '<profil></profil>'}
const PregledSvihKorisnika = { template: '<pregled-svih-korisnika></pregled-svih-korisnika>'}
const NovaManifestacija = { template: '<nova-manifestacija></nova-manifestacija>'}
const KarteZaManifestaciju = { template: '<karte-za-manifestaciju></karte-za-manifestaciju>'}
const Manifestacija = { template: '<manifestacija></manifestacija>'}
const ManifestacijeProdavca = { template: '<manifestacije-prodavca></manifestacije-prodavca>'}
const KarteKupca = { template: '<karte-kupca></karte-kupca>'}
const Korpa = { template: '<korpa></korpa>'}

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: ManifestacijePocetna},
	    { path: '/registracija', component: Registracija },
	    { path: '/prijava', component: Prijava },
	    { path: '/odjava', component: Odjava },
	    { path: '/profil', component: Profil },
	    { path: '/pregled-svih-korisnika', component: PregledSvihKorisnika},
	    { path: '/karte-za-manifestaciju', component: KarteZaManifestaciju},
	    { path: '/manifestacija', component: Manifestacija},
	    { path: '/nova-manifestacija', component: NovaManifestacija},
	    { path: '/manifestacije-prodavca', component: ManifestacijeProdavca},
	    { path: '/karte-kupca', component: KarteKupca},
	    { path: '/korpa', component: Korpa}
	  ]
});

var app = new Vue({
	router,
	el: '#webShop'
});