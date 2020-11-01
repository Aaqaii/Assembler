import java.util.*;
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.Map; 
import java.math.BigInteger;
public class assembler{
	//to convert hexadecimal to binary
	public static String HexaConvert(String str,int k){
		String num = new BigInteger(str, 16).toString(2);
		while(num.length()<k)
			num=0+num;
		return num;
	}
		//to convert integer to binary 
	public static String convert(int rd){
		String ans=Integer.toString(rd, 2);
		while(ans.length()<5)
			 ans=0+ans;
		 return ans;
	}
	//to convert signed integer to binary 
	public static String convertI(int imm){
			String ans=Integer.toBinaryString(imm);
			int k=0;
			if(imm<0)
				k=1;
			
				while(ans.length()<12)
					 ans=k+ans;
				 if(ans.length()>12)
					 return ans.substring(ans.length()-12);
			
			return ans;
					
	}
	//convert R format instructions to binary
	public static String Rformet(ArrayList<String> input,HashMap<String,String> R){
	
String out="0110011"; //optcode
int rd=Integer.parseInt(input.get(1).substring(1));
int rs1=Integer.parseInt(input.get(2).substring(1));
int rs2=Integer.parseInt(input.get(3).substring(1));
out=convert(rs2)+" "+convert(rs1)+" "+R.get(input.get(0))
+" "+convert(rd)+" "+out;//adding destination address,source address
if(input.get(0).equals("sub")||input.get(0).equals("sra")){
	out="0100000 "+out;
}
else
out="0000000 "+out;
return out;
	}
	//convert I format instructions to binary
	public static String Iaformet(ArrayList<String> input,HashMap<String,String> Ia,HashMap<String,String> Ib){
		String out="0010011";
		if(Ib.containsKey(input.get(0)))
			out="0000011";
		if(input.get(0).equals("jalr"))
			out="1100111";
		int rd=Integer.parseInt(input.get(1).substring(1));
		int rs1=Integer.parseInt(input.get(2).substring(1));
		int imm=Integer.parseInt(input.get(3));
		out=convertI(imm)+" "+convert(rs1)+" "+Ia.get(input.get(0))+" "+convert(rd)+" "+out;
		if(input.get(0).equals("srai"))
			out="01"+out.substring(2);
		System.out.println(imm);
		return out;
		
	}
	//convert S format instructions to binary
	public static String Sformet(ArrayList<String> input,HashMap<String,String> S){
		String out="0100011";
		int rs1=Integer.parseInt(input.get(1).substring(1));
		int rs2=Integer.parseInt(input.get(2).substring(1));
		int imm=Integer.parseInt(input.get(3));
		String immediate=convertI(imm);
		out=immediate.substring(0,7)+" "+convert(rs2)+" "+convert(rs1)+" "+
		S.get(input.get(0))+" "+immediate.substring(7)+" "+out;
		return out;
	}
	//convert U format instructions to binary
	public static String Uformat(ArrayList<String> input){
		String out="0110111";
		if(input.get(0).equals("auipc"))
			out="0010111";
		int rd=Integer.parseInt(input.get(1).substring(1));
		 String num =HexaConvert(input.get(2),20);
		out=num+" "+convert(rd)+" "+out;
		
return out;
	}
	//convert B format instructions to binary
	public static String Bformat(ArrayList<String> input,HashMap<String,String> B){
		String out="1100011";
		 String num =HexaConvert(input.get(3),12);
		 int rs1=Integer.parseInt(input.get(1).substring(1));
        int rs2=Integer.parseInt(input.get(2).substring(1));
		
		out=num.charAt(0)+num.substring(2,8)+" "+convert(rs2)+" "+
		convert(rs1)+" "+B.get(input.get(0))+" "+num.substring(8)+num.charAt(1)+" "+out;
		return out;
		
	}
	//convert UJ format instructions to binary
	public static String UJformat(ArrayList<String> input){
		String out="1101111";
		 int rd=Integer.parseInt(input.get(1).substring(1));
		 String num =HexaConvert(input.get(2),20);
		 out=num.charAt(0)+num.substring(10)+num.charAt(9)+num.substring(1,9)+" "+
		 convert(rd)+" "+out;
		return out;	 
	}
		
	public static void main(String[] args){
		Scanner s=new Scanner(System.in);
		String str =s.nextLine();
		ArrayList<String>input =new ArrayList<>();
		int start=0;
		String out="";
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)==' '){
				input.add(str.substring(start,i));
				start=i+1;
			}
			if(i==str.length()-1)
				input.add(str.substring(start));
				
		}

	HashMap<String, String> R = new HashMap<>();
		R.put("add","000");
		R.put("sub","000");
		R.put("sll","001");
		R.put("slt","010");
		R.put("sltu","011");
		R.put("xor","100");
		R.put("srl","101");
		R.put("sra","101");
		R.put("or","110");
		R.put("and","111");
		HashMap<String, String> Ia = new HashMap<>();
		Ia.put("addi","000");
		Ia.put("slti","010");
		Ia.put("sltiu","011");
		Ia.put("ori","110");
		Ia.put("xori","100");
		Ia.put("andi","111");
		Ia.put("slli","001");
		Ia.put("srli","001");
		Ia.put("srai","101");
		Ia.put("jalr","000");
		HashMap<String, String> Ib = new HashMap<>();
		Ib.put("lb","000");
		Ib.put("lh","001");
		Ib.put("lw","010");
		Ib.put("lbu","100");
		Ib.put("lhu","101");
		HashMap<String, String> S = new HashMap<>();
		S.put("sb","000");
		S.put("sh","001");
		S.put("sw","010");
		HashMap<String, String> B = new HashMap<>();
		B.put("beq","000");
		B.put("bne","001");
		B.put("blt","100");
		B.put("bge","101");
		B.put("bltu","110");
		B.put("begu","111");
		
		
		
if(R.containsKey(input.get(0)))
			out=Rformet(input,R);
else if(Ib.containsKey(input.get(0))||Ia.containsKey(input.get(0)))
		out=Iaformet(input,Ia,Ib);
	else if(S.containsKey(input.get(0)))
			out=Sformet(input,S);
		//U format instruction "lui" and "auipc"
		else if(input.get(0).equals("lui")||input.get(0).equals("auipc"))
			out=Uformat(input);
		else if(B.containsKey(input.get(0)))
			out=Bformat(input,B);
		else if(input.get(0).equals("jal"))
		out=UJformat(input);
		else
			out="please! Enter a vaild RV32I Instruction";
			System.out.println(out);
		 
		
	}

}