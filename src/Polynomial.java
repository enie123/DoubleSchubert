import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Monomial {
	private int numVar;
	private int[] degrees;
	
	public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

	public Monomial(int numVar) { // create Monomial with a certain number of variables
		this.numVar = numVar;
		degrees = new int[numVar];
	}
	
	
	public Monomial(int[] degrees) {
		this.numVar = degrees.length;
		this.degrees = degrees;
	}

	public int getDegrees(int index) {
		if (index >= degrees.length) {
			return 0;
		}
		return degrees[index];
	}
	
	public int getNumVar(){
		return numVar;
	}

	public Monomial multiplyBy(Monomial other) {

		int[] product = new int[Math.max(degrees.length, other.degrees.length)];
		for (int i = 0; i < product.length; i++) {
			product[i] = getDegrees(i);
		}
		for (int i = 0; i < product.length; i++) {
			product[i] += other.getDegrees(i);
		}
		return new Monomial(product);
	}
	
	public Polynomial partialDerivative(Monomial monomial){
		Map<Monomial, Integer> derivative = new HashMap<Monomial, Integer>(); //contains the monomial derivative and the coefficient
		int coefficient = 1; 
		int[] copyDegrees = degrees.clone();						//maintain local copy for the derivative
		for(int i = 0; i<monomial.getNumVar(); i++ ){
			int temp = monomial.getDegrees(i);
			if(monomial.getDegrees(i)>0){  
				coefficient*=factorial(degrees[i]);
				copyDegrees[i] -= monomial.getDegrees(i);           
			}
		}
		derivative.put(new Monomial(copyDegrees), coefficient);
		return new Polynomial(derivative);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(degrees);
		result = prime * result + numVar;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monomial other = (Monomial) obj;
		if (!Arrays.equals(degrees, other.degrees))
			return false;
		if (numVar != other.numVar)
			return false;
		return true;
	}

	public String toString() {
		String out = "";
		if (degrees.length <= 26) {
			for (int i = 0; i < degrees.length; i++) {
				if (degrees[i] != 0) {
					out += (((char) (i + 'a') + "^" + degrees[i] + " "));
				}
			}
		}
		return out;
	}
}

public class Polynomial {
	private Map<Monomial, Integer> coefficients;

	// some constructors here
	public Polynomial(Monomial[] terms, int[] coefficients) { // construct by
																// terms and
																// coefficients
		this.coefficients = new HashMap<Monomial, Integer>();
		for (int i = 0; i < terms.length; i++) {
			this.coefficients.put(terms[i], coefficients[i]);
		}
	}

	public Polynomial(Map<Monomial, Integer> coefficients) {
		this.coefficients = new HashMap<Monomial, Integer>();
		for (Monomial monomial : coefficients.keySet()) {
			this.coefficients.put(monomial, coefficients.get(monomial));
		}
	}

	public Polynomial(){
		coefficients = new HashMap<Monomial, Integer>();
	}
	
	
	
	public int getCoefficient(Monomial monomial) {
		if (coefficients.containsKey(monomial)) {
			return coefficients.get(monomial);
		}
		return 0;
	}

	
	
	public Polynomial add(Polynomial anotherPolynomial){
		Map<Monomial, Integer> copyCoeff = coefficients;
		Polynomial sum = new Polynomial(copyCoeff);
		for(Monomial terms: anotherPolynomial.coefficients.keySet()){
			if(sum.coefficients.containsKey(terms)){
				sum.coefficients.put(terms, sum.coefficients.get(terms)+anotherPolynomial.coefficients.get(terms)); 
			}else{
				sum.coefficients.put(terms, anotherPolynomial.coefficients.get(terms));
			}
		}
		return sum;
	}
	
	public Polynomial multiply(int c){
		Polynomial product = new Polynomial(coefficients);
		for(Monomial monomial: coefficients.keySet()){
			coefficients.put(monomial, coefficients.get(monomial)*c);
		}
		return product;
	}
	public Polynomial multiplyBy(Polynomial anotherPolynomial) { 
		Map<Monomial, Integer> product = new HashMap<Monomial, Integer>();
		//check first to see if one of polynomials is identity
			for (Monomial firstM : coefficients.keySet()) {
				for (Monomial secondM : anotherPolynomial.coefficients.keySet()) {
					Monomial curProduct = firstM.multiplyBy(secondM);
					int curCoefficient = coefficients.get(firstM)
							* anotherPolynomial.coefficients.get(secondM);
					if (product.containsKey(curProduct)) {
						int newCoeff = product.get(curProduct) + curCoefficient;
						if(newCoeff!=0){
							product.put(curProduct, product.get(curProduct) + curCoefficient);
						}// if term exists add coefficient
													// onto existing one
					} else {
						product.put(curProduct, curCoefficient); // otherwise create
																	// the term and
																	// set the
																	// coefficient
					}
				}
				
			}
		return new Polynomial(product);
	}
	
	public Polynomial partialDerivative(Polynomial anotherPolynomial){
		Polynomial derivative = new Polynomial();
		for(Monomial terms: anotherPolynomial.coefficients.keySet()){
			derivative = derivative.add(partialDerivative(terms));
		}
		return derivative;
	}
	public Polynomial partialDerivative(Monomial monomial){ //f(d/dx, d/dy, ...)*g
		Polynomial derivative = new Polynomial();
		for(Monomial terms: coefficients.keySet()){
			derivative = derivative.add(terms.partialDerivative(monomial));
		}
		return derivative;
	}	

	
	
	public String toString() {
		String out = "";
		if(!coefficients.keySet().isEmpty()){
		for (Monomial monomial : coefficients.keySet()) {
			out += (coefficients.get(monomial) + monomial.toString() + " + ");
		}
			out = out.substring(0, out.length() - 2);  // cut out extra + sign
		}
		return out;
	}

	// and so on
	// test client
	public static void main(String[] args) {

		Monomial y1 = new Monomial(new int[] { 1, 0, 0, 0 }); //y_1
		Monomial y2 = new Monomial(new int[] { 0, 1, 0, 0 }); //y_2
		Monomial y3 = new Monomial(new int[] { 0, 0, 1, 0 }); //y_3
		Monomial y4 = new Monomial(new int[] { 0, 0, 0, 1}); //y_4
		Polynomial p = new Polynomial(new Monomial[]{y1, y2}, new int[]{1, 1}); //y_1 + y_2
		Polynomial id = new Polynomial();
		Polynomial q = new Polynomial(new Monomial[]{y1.multiplyBy(y1).multiplyBy(y2)}, new int[]{1}); //y_1^2 * y_2
		System.out.println(q.partialDerivative(new Polynomial(new Monomial[]{y1, y2}, new int[]{1, 1})));

		Monomial[] y = new Monomial[4];
		for(int i = 0; i<y.length; i++){					   //initialize the array of variables
			int[] curMonomial = new int[y.length];
			for(int j = 0; j<y.length; j++){
				curMonomial[j] = (i%y.length==j)?1:0;          //if j mod the num of variables is equal to j set to 1
			}
			y[i] = new Monomial(curMonomial);
		}

		Polynomial w0 = new Polynomial(new Monomial[]{y[0], y[1]}, new int[]{1, -1}); 
		for(int i =0; i<y.length; i++){
			for(int j =i+1; j<y.length; j++){
				if(i!=0||j!=1){
					w0 = w0.multiplyBy(new Polynomial(new Monomial[]{y[i], y[j]}, new int[]{1,-1}));
				}
			}
		}
	}

}
