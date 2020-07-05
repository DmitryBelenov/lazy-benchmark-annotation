import annotation.LazyBenchmark;

public class BenchmarkTest {

    public static void main(String[] args) {
        Object[] customRefTypes = new Object[]{new CustomReferenceType("Hello world")};
        new Benchmark(customRefTypes, TypesTesting.class);
    }

    private static class CustomReferenceType {

        private String message;

        CustomReferenceType() {
        }

        CustomReferenceType(String message) {
            this.message = message;
        }

        private void sayHello(){
            System.out.println(message);
        }
    }

    private static class TypesTesting {
        public TypesTesting() {
        }
        @LazyBenchmark(priority = 1)
        private void doString(String s){

        }

        @LazyBenchmark(priority = 2)
        private void doRefByte(Byte b){

        }

        @LazyBenchmark(priority = 3)
        private void doRefShort(Short s){

        }

        @LazyBenchmark(priority = 4)
        private void doRefInteger(Integer i){

        }

        @LazyBenchmark(priority = 5)
        private void doRefLong(Long l){

        }

        @LazyBenchmark(priority = 6)
        private void doRefCharacter(Character ch){

        }

        @LazyBenchmark(priority = 7)
        private void doRefFloat(Float f){

        }

        @LazyBenchmark(priority = 8)
        private void doRefDouble(Double d){

        }

        @LazyBenchmark(priority = 9)
        private void doRefBoolean(Boolean b){

        }

        @LazyBenchmark(priority = 10)
        private void doStrings(String[] s){

        }

        @LazyBenchmark(priority = 11)
        private void doRefBytes(Byte[] b){

        }

        @LazyBenchmark(priority = 12)
        private void doRefShorts(Short[] s){

        }

        @LazyBenchmark(priority = 13)
        private void doRefIntegers(Integer[] i){

        }

        @LazyBenchmark(priority = 14)
        private void doRefLongs(Long[] l){

        }

        @LazyBenchmark(priority = 15)
        private void doRefCharacters(Character[] ch){

        }

        @LazyBenchmark(priority = 16)
        private void doRefFloats(Float[] f){

        }

        @LazyBenchmark(priority = 17)
        private void doRefDoubles(Double[] d){

        }

        @LazyBenchmark(priority = 18)
        private void doRefBooleans(Boolean[] b){

        }

        @LazyBenchmark(priority = 19)
        private void doByte(byte b){

        }

        @LazyBenchmark(priority = 20)
        private void doShort(short s){

        }

        @LazyBenchmark(priority = 21)
        private void doInteger(int i){

        }

        @LazyBenchmark(priority = 22)
        private void doLong(long l){

        }

        @LazyBenchmark(priority = 23)
        private void doCharacter(char ch){

        }

        @LazyBenchmark(priority = 24)
        private void doFloat(float f){

        }

        @LazyBenchmark(priority = 25)
        private void doDouble(double d){

        }

        @LazyBenchmark(priority = 26)
        private void doBoolean(boolean b){

        }

        @LazyBenchmark(priority = 27)
        private void doBytes(byte[] b){

        }

        @LazyBenchmark(priority = 28)
        private void doShorts(short[] s){

        }

        @LazyBenchmark(priority = 29)
        private void doIntegers(int[] i){

        }

        @LazyBenchmark(priority = 30)
        private void doLongs(long[] l){

        }

        @LazyBenchmark(priority = 31)
        private void doCharacters(char[] ch){

        }

        @LazyBenchmark(priority = 32)
        private void doFloats(float[] f){

        }

        @LazyBenchmark(priority = 33)
        private void doDoubles(double[] d){

        }

        @LazyBenchmark(priority = 34)
        private void doBooleans(boolean[] b){

        }

        @LazyBenchmark(priority = 35)
        private void doCustomReferenceType(CustomReferenceType crt){
            crt.sayHello();
        }
    }
}
