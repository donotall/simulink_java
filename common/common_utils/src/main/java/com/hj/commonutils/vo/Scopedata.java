package com.hj.commonutils.vo;

import com.sun.jna.Structure;
import lombok.Data;

@Data
public class Scopedata{
    public static class scopedata extends  Structure{
        public   int    number;
        public  int    type;
        public int    state;
        public int    signals[]=new int[20];
        public int    numsamples;
        public int    decimation;
        public int    triggermode;
        public int    numprepostsamples;
        public int    triggersignal;
        public int    triggerscope;
        public int    triggerscopesample;
        public double triggerlevel;
        public int    triggerslope;
    }
}
