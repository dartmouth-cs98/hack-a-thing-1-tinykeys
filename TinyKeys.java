

/* Copyright info for the KeyDemo code I used and modified in some places
 *
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/*
 * Based on the KeyEventDemo from the Java tutorials and Evan Merz's "Sound Synthesis in Java"
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

public class TinyKeys extends JFrame
        implements KeyListener,
        ActionListener
{
    JTextArea displayArea;
    StavePanel playedNotesArea;
    static final String newline = System.getProperty("line.separator");

    WavePlayer[] wavs;
    Gain[] gains;
    Glide[] glides;

    Float attack = 25.0f;
    Float decay = 50.0f;

    int lastKeyPressed;
    Integer[] keyWavNumber = new Integer[10];
    int wavIndex = 0;
    static TinyKeys frame;

    public static void main(String[] args) {

        // Use the metal look and feel for crossplatform
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        // turn off bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    // create and show the GUI
    // invoke from the thread dispatching class
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new TinyKeys("TinyKeys");
        frame.setup();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.addComponentsToPane();


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    // set up the comboboxes and add everything to the frame
    private void addComponentsToPane() {

        String[] waveTypes = {"Square", "Sine", "Saw", "Triangle", "Noise"};
        JComboBox waveformPicker = new JComboBox(waveTypes);
        waveformPicker.setName("waveform");
        waveformPicker.setSelectedIndex(1);
        waveformPicker.addActionListener(this);

        Float[] attackLengths = {25.0f, 125.0f, 225.0f, 325.0f, 425.0f};
        JComboBox attackPicker = new JComboBox(attackLengths);
        attackPicker.setName("attack");
        attackPicker.setSelectedIndex(0);
        attackPicker.addActionListener(this);

        Float[] decayLengths = {50.0f, 150.0f, 250.0f, 350.0f, 450.0f};
        JComboBox decayPicker = new JComboBox(decayLengths);
        decayPicker.setName("decay");
        decayPicker.setSelectedIndex(0);
        decayPicker.addActionListener(this);

        displayArea = new JTextArea();
        displayArea.addKeyListener(this);
        playedNotesArea = new StavePanel();


        // Turn off focus traversal (hitting tab to change panel focus)
        displayArea.setFocusTraversalKeysEnabled(false);

        displayArea.setEditable(false);
        displayArea.setPreferredSize(new Dimension(275, 100));


        getContentPane().add(playedNotesArea, BorderLayout.PAGE_START);
        getContentPane().add(displayArea, BorderLayout.LINE_START);
        getContentPane().add(attackPicker, BorderLayout.CENTER);
        getContentPane().add(decayPicker, BorderLayout.LINE_END);
        getContentPane().add(waveformPicker, BorderLayout.PAGE_END);
    }

    public TinyKeys(String name) {
        super(name);
    }


    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        displayArea.setText("");
        displayInfo(e, "KEY PRESSED: ");
        frame.keyDown(e.getKeyCode());
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        displayArea.setText("");
        displayInfo(e, "KEY RELEASED: ");
        frame.keyUp(e.getKeyCode());
    }

    // Handle selections in the comboboxes
    public void actionPerformed(ActionEvent e) {
        //Clear the text components.
        displayArea.setText("");

        JComboBox cb = (JComboBox)e.getSource();

        // if setting the waveform type
        if(cb.getName().equals("waveform")) {

            String waveType = (String) cb.getSelectedItem();

            if (waveType.equals("Sine")) {

                for (WavePlayer wav : wavs) {
                    wav.setBuffer(Buffer.SINE);
                }
            }

            else if (waveType.equals("Square")) {
                for (WavePlayer wav : wavs) {
                    wav.setBuffer(Buffer.SQUARE);
                }
            }

            else if (waveType.equals("Triangle")) {
                for (WavePlayer wav : wavs) {
                    wav.setBuffer(Buffer.TRIANGLE);
                }
            }

            else if (waveType.equals("Saw")) {
                for (WavePlayer wav : wavs) {
                    wav.setBuffer(Buffer.SAW);
                }
            }

            else {
                for (WavePlayer wav : wavs) {
                    wav.setBuffer(Buffer.NOISE);
                }
            }
        }

        // if altering attack length
        else if(cb.getName().equals("attack")){
            attack = (Float) cb.getSelectedItem();
        }

        // otherwise, it must be altering the decay
        else {
            decay = (Float) cb.getSelectedItem();
        }


        //Return the focus to the main area.
        displayArea.requestFocusInWindow();
    }

    // This is taken wholesale from the KeyEventDemo in the Java tutorials
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/events/index.html#KeyEventDemo

    /* Displays info about the keyevent,
    namely if it was a press or release and what key was pressed.
    */
    private void displayInfo(KeyEvent e, String keyStatus){

        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }

        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }

        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }

        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }

        displayArea.append(keyStatus + newline
                + "    " + keyString + newline
                + "    " + modString + newline
                + "    " + actionString + newline
                + "    " + locationString + newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }

    // set up all the needed oscillators, gains, and glides
    public void setup(){
        // based on examples from Evan Merz's "Sound Synthesis in Java"

        AudioContext ac = new AudioContext();

        wavs = new WavePlayer[10];
        glides = new Glide[10];
        gains = new Gain[10];

        for(int i = 0; i < 10; i++){
            wavs[i] = new WavePlayer(ac, 440.0f, Buffer.SINE);
            glides[i] = new Glide(ac, 0.0f, attack);
            gains[i] = new Gain(ac, 1, glides[i]);
            gains[i].addInput(wavs[i]);
            ac.out.addInput(gains[i]);
        }

        ac.start();
    }

    // handle key presses
    // based in part on code from Evan Merz's "Sound Synthesis in Java"
    public void keyDown(int keyCode){

        if(wavs != null && glides != null && !(Arrays.asList(keyWavNumber).contains(Integer.valueOf(keyCode)))) {

            System.out.println("Got here!");

            lastKeyPressed = keyCode;

            WavePlayer currentWav = wavs[wavIndex];
            Glide currentGlide = glides[wavIndex];
            keyWavNumber[wavIndex] = Integer.valueOf(keyCode);
            playedNotesArea.drawNotes(keyWavNumber);

            currentGlide.setGlideTime(attack);

            // set frequencies for oscillators based on what key is pressed
            if (keyCode == KeyEvent.VK_A) {
                currentWav.setFrequency(261.626f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_W) {
                currentWav.setFrequency(277.183f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_S) {
                currentWav.setFrequency(293.665f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_E) {
                currentWav.setFrequency(311.127f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_D) {
                currentWav.setFrequency(329.628f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_F) {
                currentWav.setFrequency(349.228f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_T) {
                currentWav.setFrequency(369.994f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_G) {
                currentWav.setFrequency(391.995f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_Y) {
                currentWav.setFrequency(415.305f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_H) {
                currentWav.setFrequency(440.000f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_U) {
                currentWav.setFrequency(466.164f);
                currentGlide.setValue(0.9f);
            }

            if (keyCode == KeyEvent.VK_J) {
                currentWav.setFrequency(493.883f);
                currentGlide.setValue(0.9f);
            }

            // iterate the wavIndex, returning to zero when the value would reach 10
            if (wavIndex < 9) {
                wavIndex += 1;
            }

            else {
                wavIndex = 0;
            }

        }
    }

    // handle key releases
    public void keyUp(int keyCode)
    {
        if(glides != null)
        {
            // fetch the desired glide using the info stored in keyWavNumber
            int tempIndex = Arrays.asList(keyWavNumber).indexOf(keyCode);
            keyWavNumber[tempIndex] = null;
            Glide currentGlide = glides[tempIndex];
            currentGlide.setGlideTime(decay);
            currentGlide.setValue(0.0f);
        }

        playedNotesArea.drawNotes(keyWavNumber);

    }
}
