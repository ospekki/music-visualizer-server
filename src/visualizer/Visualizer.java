package visualizer;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.github.mbelling.ws281x.LedStripType;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Visualizer {

    private static Ws281xLedStrip ledStrip;
    private static Timer timer = new Timer();
    private static Server server;
    private static int[] data = new int[256];
    private static int[] spectrumData = new int[60];
    private static float[] lineHeights = new float[60];
    private static Color[] colors = new Color[60];
    private static float[] colorVals = new float[60];
    private static double colorHue = 0.0;
    private static int ledCount = 60, effect = 0, colorM = 0, brightness = 255;
    private static int lineCount = 10, symmetric = 0;
    private static float speed1 = 0.20F, speed2 = 0.20F, lightTime = 5.0F;
    private static float minHeight = 50.0F;
    private static Color clr1 = Color.YELLOW, clr2 = Color.WHITE, clr3 = Color.BLACK;
    
    public static void main(String[] args) {
        startServer(); 
        timer1();
        
        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = new Color(0, 0, 0);
        }
    }
    
    static void startServer()
    {
        try 
        {
            server = new Server();
            server.start();
        } 
        catch (SocketException ex) 
        {
            Logger.getLogger(Visualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void setLedColor(int i, Color clr)
    {
        if (symmetric == 0)
        {
            if (ledCount > 60 && effect != 2)
            {
                int ledIdx = i * (ledCount / 60);
                ledStrip.setPixel( ledIdx, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );

                for (int j = 1; j < (ledCount / 60); j++)
                {
                    ledStrip.setPixel( ledIdx + j, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
                }
            }
            else
            {
                ledStrip.setPixel( i, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
            }
        }
        else
        {
            int lCount = ledCount / 2;
            if (lCount > 60 && effect != 2)
            {
                int ledIdx = i * (lCount / 60);
                ledStrip.setPixel( ledIdx, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );

                for (int j = 1; j < (lCount / 60); j++)
                {
                    ledStrip.setPixel( ledIdx + j, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
                }
                
                int ledIdx2 = (ledCount - (i * (lCount / 60))) - 1;
                ledStrip.setPixel( ledIdx2, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );

                for (int j = (lCount / 60) - 1; j >= 0; j--)
                {
                    ledStrip.setPixel( ledIdx2 - j, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
                }
            }
            else
            {
                ledStrip.setPixel( i, new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
                ledStrip.setPixel( ledCount - (i + 1), new Color(clr.getGreen(), clr.getRed(), clr.getBlue()) );
            }
        }
    }
    
    // Sets new configurations received from the client
    static void setConfigs(String configs)
    {
        String[] configArr = configs.split("_");
        
        if (configArr.length >= 18)
        {
            effect = Integer.parseInt(configArr[0]);
            colorM = Integer.parseInt(configArr[1]);
            minHeight = Float.parseFloat(configArr[2]);
            lightTime = Float.parseFloat(configArr[3]);
            speed1 = Float.parseFloat(configArr[4]);
            speed2 = Float.parseFloat(configArr[5]);
            brightness = Integer.parseInt(configArr[6]);
            lineCount = Integer.parseInt(configArr[7]);
            symmetric = Integer.parseInt(configArr[8]);
            
            clr1 = new Color(Integer.parseInt(configArr[9]), Integer.parseInt(configArr[10]), 
                    Integer.parseInt(configArr[11]));
            clr2 = new Color(Integer.parseInt(configArr[12]), Integer.parseInt(configArr[13]), 
                    Integer.parseInt(configArr[14]));
            clr3 = new Color(Integer.parseInt(configArr[15]), Integer.parseInt(configArr[16]), 
                    Integer.parseInt(configArr[17]));
            
            if (colorM == 0)
            {
                colorHue = 0.0;
            }
        }
    }
    
    // Receives data from the client
    public static void getData(byte[] bytes)
    {
        for (int j = 0; j < data.length; j++)
        {
            int i = bytes[j];
            if (i < 0){ i = 256 + i;}
            data[j] = i;
        }
        
        int t = data[0];
        
        // Create an LED strip with the given settings
        if (t == 50)
        {
            if (ledStrip == null)
            {
                byte[] intBytes = new byte[bytes.length - 1];

                for (int i = 1; i < intBytes.length; i++)
                {
                    intBytes[i - 1] = bytes[i];
                }

                ledCount = Integer.parseInt(new String(intBytes).split("_")[0]);

                // new Ws281xLedStrip(led count, GPIO pin, frequench Hz, DMA, 
                // brightness (0-255), PWM channel, invert, strip type, clear LEDs on exit)
                ledStrip = new Ws281xLedStrip(ledCount, 18, 800000, 10, 255, 0, 
                        false, LedStripType.WS2811_STRIP_RGB, true);
            }
        }
        
        // Sets new configurations
        if (t == 51)
        {
            byte[] configBytes = new byte[bytes.length - 1];
            
            for (int i = 1; i < configBytes.length; i++)
            {
                configBytes[i - 1] = bytes[i];
            }
            
            setConfigs(new String(configBytes));
        }
        
        if (t == 0 || t == 1 || t == 2)
        {
            if (ledStrip != null)
            {
                for (int i = 1; i < 61; i++)
                {
                    spectrumData[i - 1] = data[i];
                }
            }
        }
    }  
    
    // The timer is used to call "BlinkingLeds", "DimmingLeds" and "Lines" 
    // methods every 10 milliseconds
    static void timer1 ()
    {
        timer.schedule(new TimerTask() 
        {
            @Override
            public void run() 
            {
                if (ledStrip != null)
                {
                    if (effect == 0)
                    {
                        blinkingLeds();
                    }
                    
                    if (effect == 1)
                    {
                        dimmingLeds();
                    }
                    
                    if (effect == 2)
                    {
                        lines();
                    }
                    
                    if (colorM == 1)
                    {
                        colorHue += 0.15;
                    }
                    
                    ledStrip.render();
                }
                
                timer1();
            }
            
        }, 10);
    }
    
    // Mixes two colors
    static Color blend(Color colorA, Color colorB, float a)
    {
        if (a > 255.0F) { a = 255.0F; }
        float amount = 1.0F - (a / 255.0F);

        float r = ((colorA.getRed() * amount) + colorB.getRed() * (1 - amount));
        float g = ((colorA.getGreen()* amount) + colorB.getGreen() * (1 - amount));
        float b = ((colorA.getBlue() * amount) + colorB.getBlue() * (1 - amount));

        if (r < 0) { r = 0; }
        if (g < 0) { g = 0; }
        if (b < 0) { b = 0; }

        return new Color(Math.round(r), Math.round(g), Math.round(b));
    }
    
    public static Color colorFromHSV(double hue, double saturation, double value)
    {
        int hi = (int) (Math.round(Math.floor(hue / 60)) % 6);
        double f = hue / 60 - Math.floor(hue / 60);

        value = value * 255;
        int v = (int) Math.round(value);
        int p = (int) Math.round(value * (1 - saturation));
        int q = (int) Math.round(value * (1 - f * saturation));
        int t = (int) Math.round(value * (1 - (1 - f) * saturation));

        if (hi == 0)
            return new Color(v, t, p);
        else if (hi == 1)
            return new Color(q, v, p);
        else if (hi == 2)
            return new Color(p, v, t);
        else if (hi == 3)
            return new Color(p, q, v);
        else if (hi == 4)
            return new Color(t, p, v);
        else
            return new Color(v, p, q);
    }
    
    
    static Color getColor(int idx)
    {
        Color clr = new Color(0, 0, 0);
        colorVals[idx] = 255.0F;
        
        if (colorM == 0)
        {
            clr = blend(clr1, clr2, spectrumData[idx] - minHeight);
        }
        
        if (colorM == 1)
        {
            clr = blend(colorFromHSV(colorHue, 1.0F, 1.0F), 
                    colorFromHSV(colorHue, 0.4F, 1.0F), spectrumData[idx] - minHeight);
        }
        
        return clr;
    }
    
    static void blinkingLeds()
    {
        for (int i = 0; i < 60; i++)
        {
            if (spectrumData[i] > lineHeights[i])
            {
                lineHeights[i] += speed1 * (spectrumData[i] - lineHeights[i]);

                if (lineHeights[i] >= minHeight)
                {
                    colors[i] = blend(Color.BLACK, getColor(i), brightness);
                    setLedColor(i, colors[i]);
                }
            }

            if (spectrumData[i] < lineHeights[i])
            {
                lineHeights[i] -= speed2 * (lineHeights[i] - spectrumData[i]);

                if (spectrumData[i] + lightTime < lineHeights[i] || lineHeights[i] <= 0.1F)
                {
                    Color newClr = clr3;
                    colors[i] = blend(Color.BLACK, newClr, brightness);
                    setLedColor(i, colors[i]);
                }
            }
        }
    } 
    
    static void dimmingLeds()
    {
        for (int i = 0; i < 60; i++)
        {
            if (spectrumData[i] > lineHeights[i])
            {
                lineHeights[i] += speed1 * (spectrumData[i] - lineHeights[i]);

                if (lineHeights[i] >= minHeight)
                {
                    colors[i] = blend(Color.BLACK, getColor(i), brightness);
                    colorVals[i] = brightness;
                    setLedColor(i, colors[i]);
                }
            }

            if (spectrumData[i] < lineHeights[i])
            {
                lineHeights[i] -= speed2 * (lineHeights[i] - spectrumData[i]);

                if (colorVals[i] > 0.0F)
                {
                    if (spectrumData[i] + lightTime < lineHeights[i] || lineHeights[i] <= 0.1F)
                    {
                        colorVals[i] -= 20.0F * (brightness / 255.0F);
                        
                        if (colors[i] != null)
                        {
                            Color color = blend(clr3, colors[i], colorVals[i]);
                            setLedColor(i, color);
                        }
                    }
                }
            }
        }
    }
    
    static void lines()
    {
        for (int i = 0; i < lineCount; i++)
        {
            int idx = i * (60 / lineCount);
            if (spectrumData[idx] > lineHeights[idx])
            {
                lineHeights[idx] += speed1 * (spectrumData[idx] - lineHeights[idx]);
            }

            if (spectrumData[idx] < lineHeights[idx])
            {
                lineHeights[idx] -= speed2 * (lineHeights[idx] - spectrumData[idx]);
            }
            
            int length;
            
            if (symmetric == 0)
            {
                length = ledCount / lineCount;
            }
            else
            {
                length = (ledCount / 2) / lineCount;
            }
            
            int startIdx = i * length;
            float lineHeight = lineHeights[idx] * ((float)length / 255.0F);
            
            for (int j = startIdx; j < (startIdx + length); j++)
            {
                if ((j - startIdx) <= lineHeight)
                {
                    setLedColor(j, getColor(idx));
                }
                else
                {
                    setLedColor(j, clr3);
                }
            }
        }
    }
    
}
