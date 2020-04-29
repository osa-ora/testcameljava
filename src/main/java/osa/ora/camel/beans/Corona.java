package osa.ora.camel.beans;

public class Corona {

		String country;	
		int cases;
		int todayCases;		
		int deaths;	
		int todayDeaths;	
		int recovered;	
		int active;	
		int critical;	
		int casesPerOneMillion;	
		int deathsPerOneMillion;	
		int totalTests;
		int testsPerOneMillion;

		public Corona() {
			
		}
		@Override
		public String toString() {
			return "{\"cases\": " + cases + ", \"country\":\"" + country + "\", \"deaths\":"+
					deaths+"}";
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public int getCases() {
			return cases;
		}
		public void setCases(int cases) {
			this.cases = cases;
		}
		public int getTodayCases() {
			return todayCases;
		}
		public void setTodayCases(int todayCases) {
			this.todayCases = todayCases;
		}
		public int getDeaths() {
			return deaths;
		}
		public void setDeaths(int deaths) {
			this.deaths = deaths;
		}
		public int getTodayDeaths() {
			return todayDeaths;
		}
		public void setTodayDeaths(int todayDeaths) {
			this.todayDeaths = todayDeaths;
		}
		public int getRecovered() {
			return recovered;
		}
		public void setRecovered(int recovered) {
			this.recovered = recovered;
		}
		public int getActive() {
			return active;
		}
		public void setActive(int active) {
			this.active = active;
		}
		public int getCritical() {
			return critical;
		}
		public void setCritical(int critical) {
			this.critical = critical;
		}
		public int getCasesPerOneMillion() {
			return casesPerOneMillion;
		}
		public void setCasesPerOneMillion(int casesPerOneMillion) {
			this.casesPerOneMillion = casesPerOneMillion;
		}
		public int getDeathsPerOneMillion() {
			return deathsPerOneMillion;
		}
		public void setDeathsPerOneMillion(int deathsPerOneMillion) {
			this.deathsPerOneMillion = deathsPerOneMillion;
		}
		public int getTotalTests() {
			return totalTests;
		}
		public void setTotalTests(int totalTests) {
			this.totalTests = totalTests;
		}
		public int getTestsPerOneMillion() {
			return testsPerOneMillion;
		}
		public void setTestsPerOneMillion(int testsPerOneMillion) {
			this.testsPerOneMillion = testsPerOneMillion;
		}
				
}
