import java.util.Arrays;



public class Permutation { //everything is 1-indexed
	
	private int[] values;									//
	private int size;										//length of permutation
	private int[] inverse;    								//inverse of permutation
	private int[] lehmerCode;
	
	public Permutation(int size){											
		this.size = size;
		values = new int[size+1];
		inverse = new int[size+1];
		lehmerCode = new int[size+1];
	}
	
	public Permutation(int[] lehmerCode){									//Permutation based on lehmerCode
		size = lehmerCode.length;
		values = new int[size+1];
		inverse = new int[size+1];
		this.lehmerCode = new int[size+1];
		System.arraycopy(lehmerCode, 0, values, 1, size);
		size = values.length+1;
		for(int i = values.length-1; i>=0; i--){
			for(int j = i+1; j<values.length; j++){
				if(values[j]>=values[i]){
					values[j]++;
				}
			}
		}
	}
	
	public void setValue(int index, int val){				//set values in permutation
		values[index] = val;
		inverse[val] = index;
	}
	public void setValue(int[] values){
		System.arraycopy(values, 0, this.values, 1, size); //changing the input array to 1-index
		for(int i = 1; i<=size; i++){
			inverse[values[i-1]] = i;							//set inverse
		}
		lehmerCode = getLehmerCode();
	}
	
	
	public int get(int index){								//return o(index)
		return values[index];
	}
	public int[] getPermutation(){								//return permutation in an array
		return values;
	}
	public int getInv(int index){								//return o^(-1) (index) 
		return inverse[index];
	}
	public int[] getLehmerCode(){
		int[] code = new int[size+1];
		for(int i = 1; i<=size; i++){							//loop through all pairs of indices
			for(int j = i+1; j<=size; j++){
				if(values[i]>values[j]) code[i]++;
			}
		}
		lehmerCode = code;										//set the lehmerCode of permutation
		return code;
	}
	public int[] getFlag(){
		int[] flag = new int[size+1];
		for(int i = 1; i<=size; i++){
			for(int j = i+1; j<=size; j++){
				if(values[i]>values[j]){
					flag[i] = j-1;
					break;
				}
			}
		}
		Arrays.sort(flag);
		return flag;
	}
	public int[] getShape(){
		if(lehmerCode.length!=0){
			int[] shape = lehmerCode;
			Arrays.sort(shape);
			for( int i = 1; i < shape.length/2 + 1; ++i ) 
			{ 
			  int temp = shape[i]; 
			  shape[i] = shape[shape.length - i]; 
			  shape[shape.length - i] = temp; 
			}
			return shape;
		}else{
			getLehmerCode();
			return getShape();
		}
	}
	
	public String toString(){
		String out = "[";
		for(int i = 1; i<values.length; i++){
			out += values[i];
			if(i<values.length-1) out+=", ";
		}
		out+="]";
		return out;
	}
	
	public static void main(String[] args){
		Permutation p = new Permutation(4);
		p.setValue(new int[]{4,3,2,1});
		System.out.println();
		for(int a: p.getShape()){
			System.out.println(a);
		}
	}
}
