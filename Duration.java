package klavir;

public class Duration {
	private	int denominator, numerator; // numerator:denuminator
		
	
	public Duration(int numerator, int denominator) {
		super();
		//if (denominator != 4 && denominator != 8) throw 	GRESKA
		this.denominator = denominator;
		this.numerator = numerator;
	}


		
	public Duration(double d){
		int dur = (int) (d * 8);
		if ((dur & 1)==1) {
			//Duration(dur , 8);
			numerator = dur;
			denominator = 8;
		}
		else {
			//Duration(dur / 2, 4);
			numerator = dur / 2;
			denominator = 4;
		}
	}

		

		public int getDenominator() {
			return denominator;
		}



		public int getNumerator() {
			return numerator;
		}

		


		boolean manje (Duration d) {
			return ((numerator +0.0)/ denominator) < ((d.numerator+0.0) / d.denominator);
		}
		

		boolean vece (Duration d) {
			return ((numerator + 0.0) / denominator) > ((d.numerator + 0.0) / d.denominator);
		}

		boolean jednako (Duration d)  {
			return ((numerator == d.numerator && denominator == d.denominator)|| (numerator == 2*d.numerator && denominator == 2*d.denominator));
			
		}

		boolean razlicito (Duration d) {
			return !(jednako(d));
		}

		boolean vececIliJednako (Duration d)  {
			return !(manje(d));
		}

		boolean manjeIliJednako(Duration d) {
			return !vece(d);
		}
//		Duration double() const {
//			return (numerator + 0.0) / denominator;
//		}
		Duration minus (Duration d){
			return new Duration(Math.abs(((numerator + 0.0) / denominator) - ((d.numerator + 0.0) / d.denominator)));
		}

		Duration plus (Duration d){
			return new Duration(((numerator + 0.0) / denominator) + ((d.numerator + 0.0) / d.denominator));
		}

		Duration plusJednako(Duration d) {
			return plus(d);
		}

		Duration minusJednako(Duration d) {
			return minus(d);
		}

		/*friend ostream& operator << (ostream& it, const Duration & d) {
			return it << d.numerator << "/" << d.denominator;
		}

		friend istream& operator >> (istream& ut, Duration& d) {
			string x;
			ut >> x;
			regex rx ("([0-9]+)([^0-9]+)([0-9]+)");
			smatch result;
			regex_match(x, result, rx);
			d = Duration(stoi(result.str(1)), stoi(result.str(3)));
			return ut;
		}*/
}
