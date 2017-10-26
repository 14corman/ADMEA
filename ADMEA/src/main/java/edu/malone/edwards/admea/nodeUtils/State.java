/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.nodeUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Cory Edwards
 * @param <K>
 */
public class State<K> implements Serializable
{
    public final K object;
    
    public State(K state)
    {
        object = state;
    }
    
    @Override
    public int hashCode()
    {
        int result = 1;
        int c = 0;
        
        if(object == null)
        {
            result = 37 * result + c;
            return result;
        }
        
        else if(object instanceof Boolean)
            c = ((Boolean) object ? 0 : 1);
        
        else if(object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer)
            c = (Integer) object;
        
        else if(object instanceof Long)
            c = (int)((Long) object ^ ((Long) object >>> 32));
        
        else if(object instanceof Float)
            c = Float.floatToIntBits((Float) object);
        
        else if(object instanceof Double)
        {
            long temp = Double.doubleToLongBits((Double) object);
            c = (int)(temp ^ (temp >>> 32));
        }
        
        else if(object instanceof boolean[])
        {
            c = Arrays.hashCode((boolean[]) object);
//            boolean[] tempArray = (boolean[]) object;
//            int i = 0;
//            for(boolean temp : tempArray)
//            {
//                c += hashCode((Boolean) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof byte[])
        {
            c = Arrays.hashCode((byte[]) object);
//            byte[] tempArray = (byte[]) object;
//            int i = 0;
//            for(byte temp : tempArray)
//            {
//                c += hashCode((Byte) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof short[])
        {
            c = Arrays.hashCode((short[]) object);
//            short[] tempArray = (short[]) object;
//            int i = 0;
//            for(short temp : tempArray)
//            {
//                c += hashCode((Short) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof char[])
        {
            c = Arrays.hashCode((char[]) object);
//            char[] tempArray = (char[]) object;
//            int i = 0;
//            for(char temp : tempArray)
//            {
//                c += hashCode((Character) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof int[])
        {
            c = Arrays.hashCode((int[]) object);
//            int[] tempArray = (int[]) object;
//            int i = 0;
//            for(int temp : tempArray)
//            {
//                c += hashCode((Integer) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof long[])
        {
            c = Arrays.hashCode((long[]) object);
//            long[] tempArray = (long[]) object;
//            int i = 0;
//            for(long temp : tempArray)
//            {
//                c += hashCode((Long) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof float[])
        {
            c = Arrays.hashCode((float[]) object);
//            float[] tempArray = (float[]) object;
//            int i = 0;
//            for(float temp : tempArray)
//            {
//                c += hashCode((Float) temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof double[])
        {
            c = Arrays.hashCode((double[]) object);
//            double[] tempArray = (double[]) object;
//            int i = 0;
//            for(double temp : tempArray)
//            {
//                c += hashCode((Double) temp) + i;
//                i++;
//            }
        }
        
//        else if(object instanceof boolean[][])
//        {
//            boolean[][] tempArray = (boolean[][]) object;
//            int i = 0;
//            for(boolean[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof byte[][])
//        {
//            byte[][] tempArray = (byte[][]) object;
//            int i = 0;
//            for(byte[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof short[][])
//        {
//            short[][] tempArray = (short[][]) object;
//            int i = 0;
//            for(short[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof char[][])
//        {
//            char[][] tempArray = (char[][]) object;
//            int i = 0;
//            for(char[] temp : tempArray)
//            {
//                c += hashCode( temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof int[][])
//        {
//            int[][] tempArray = (int[][]) object;
//            int i = 0;
//            for(int[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof long[][])
//        {
//            long[][] tempArray = (long[][]) object;
//            int i = 0;
//            for(long[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof float[][])
//        {
//            float[][] tempArray = (float[][]) object;
//            int i = 0;
//            for(float[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
//        
//        else if(object instanceof double[][])
//        {
//            double[][] tempArray = (double[][]) object;
//            int i = 0 ;
//            for(double[] temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
//        }
        
        else if(object instanceof Object[])
        {
            c = Arrays.deepHashCode((Object[]) object);
//            Object[] tempArray = (Object[]) object;
//            int i = 0;
//            for(Object temp : tempArray)
//            {
//                c += hashCode(temp) + i;
//                i++;
//            }
        }
        
        else if(object instanceof Object)
            c = object.hashCode();
        
        result = 37 * result + c;
        return result;
    }
    
    private <T> int hashCode(T tempObject)
    {
        int result = 1;
        int c = 0;
        
        if(tempObject == null)
        {
            result = 37 * result + c;
            return result;
        }
        
        else if(tempObject instanceof Boolean)
            c = ((Boolean) tempObject ? 0 : 1);
        
        else if(tempObject instanceof Byte || tempObject instanceof Character || tempObject instanceof Short || tempObject instanceof Integer)
            c = (Integer) tempObject;
        
        else if(tempObject instanceof Long)
            c = (int)((Long) tempObject ^ ((Long) tempObject >>> 32));
        
        else if(tempObject instanceof Float)
            c = Float.floatToIntBits((Float) tempObject);
        
        else if(tempObject instanceof Double)
        {
            long temp = Double.doubleToLongBits((Double) tempObject);
            c = (int)(temp ^ (temp >>> 32));
        }
        
        else if(tempObject instanceof boolean[])
        {
            boolean[] tempArray = (boolean[]) tempObject;
            int i = 0;
            for(boolean temp : tempArray)
            {
                c += hashCode((Boolean) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof byte[])
        {
            byte[] tempArray = (byte[]) tempObject;
            int i = 0;
            for(byte temp : tempArray)
            {
                c += hashCode((Byte) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof short[])
        {
            short[] tempArray = (short[]) tempObject;
            int i = 0;
            for(short temp : tempArray)
            {
                c += hashCode((Short) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof char[])
        {
            char[] tempArray = (char[]) tempObject;
            int i = 0;
            for(char temp : tempArray)
            {
                c += hashCode((Character) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof int[])
        {
            int[] tempArray = (int[]) tempObject;
            int i = 0;
            for(int temp : tempArray)
            {
                c += hashCode((Integer) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof long[])
        {
            long[] tempArray = (long[]) tempObject;
            int i = 0;
            for(long temp : tempArray)
            {
                c += hashCode((Long) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof float[])
        {
            float[] tempArray = (float[]) tempObject;
            int i = 0;
            for(float temp : tempArray)
            {
                c += hashCode((Float) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof double[])
        {
            double[] tempArray = (double[]) tempObject;
            int i = 0;
            for(double temp : tempArray)
            {
                c += hashCode((Double) temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof boolean[][])
        {
            boolean[][] tempArray = (boolean[][]) tempObject;
            int i = 0;
            for(boolean[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof byte[][])
        {
            byte[][] tempArray = (byte[][]) tempObject;
            int i = 0;
            for(byte[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof short[][])
        {
            short[][] tempArray = (short[][]) tempObject;
            int i = 0;
            for(short[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof char[][])
        {
            char[][] tempArray = (char[][]) tempObject;
            int i = 0;
            for(char[] temp : tempArray)
            {
                c += hashCode( temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof int[][])
        {
            int[][] tempArray = (int[][]) tempObject;
            int i = 0;
            for(int[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof long[][])
        {
            long[][] tempArray = (long[][]) tempObject;
            int i = 0;
            for(long[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof float[][])
        {
            float[][] tempArray = (float[][]) tempObject;
            int i = 0;
            for(float[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof double[][])
        {
            double[][] tempArray = (double[][]) tempObject;
            int i = 0 ;
            for(double[] temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof Object[])
        {
            Object[] tempArray = (Object[]) tempObject;
            int i = 0;
            for(Object temp : tempArray)
            {
                c += hashCode(temp) + i;
                i++;
            }
        }
        
        else if(tempObject instanceof Object)
            c = tempObject.hashCode();
        
        result = 37 * result + c;
        return result;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(obj == null)
            return false;
        
        if(this == obj) 
            return true;
        
        if(getClass() != obj.getClass())
            return false;
            
        final State<K> other = (State<K>) obj;
        return hashCode() == other.hashCode();
    }
}
