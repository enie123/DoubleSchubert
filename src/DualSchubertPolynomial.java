
public class DualSchubertPolynomial {
	private int degrees;
	private Permutation permutation;
	
	public DualSchubertPolynomial(){
		
	}
	
	public static Polynomial w0(int degrees){
		Monomial[] y = new Monomial[degrees];
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
					System.err.println(w0);
				}
			}
		}
		return w0;
	}
	public static void main(String[] args){
		System.out.println(DualSchubertPolynomial.w0(3));
	}
}
