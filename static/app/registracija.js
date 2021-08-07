Vue.component("registracija", {
	data: function () {
		    return {
		    }
	},
	template: ` 
<div>
		<table>
			<tr>
				<td> <h2>Korisničko ime:</h2> </td> <td> <input type="text" name="korisnickoIme"/> </td>
			</tr>
			<tr>
				<td> <h2>Lozinka:</h2> </td> <td> <input type="text" name="lozinka"/> </td>
			</tr>
			<tr>
				<td> <h2>Ime:</h2> </td> <td> <input type="text" name="ime"/> </td>
			</tr>
			<tr>
				<td> <h2>Prezime:</h2> </td> <td> <input type="text" name="prezime"/> </td>
			</tr>
			<tr>
				<td> <h2>Pol:</h2> </td>
				<td>
					<select name="pol" id="pol" >
					  <option value="ZENSKI">ŽENSKI</option>
					  <option value="MUSKI">MUŠKI</option>
					</select>
				</td> 
			</tr>
			<tr>
				<td> <h2> Datum rođenja: </h2> </td>
				<td> <input type="date" id="datumRodjenja" name="datumRodjenja"> </td>
			</tr>
			<tr>
				<td align=center colspan=2>
					<input type="button" value="Potvrdi"/>
				</td>
			</tr>
		</table>
	
</div>		  
`
});