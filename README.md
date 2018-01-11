# TinyKeys, a basic additive synth

TinyKeys uses the Beads library to create oscillators, gain controls, and glides with up to 10 note polyphony supported.
This allows the use of square, sine, saw, and triangle waveforms for playing notes, in addition to noise for sound effects.
TinyKeys uses the swing framework for its GUI and to display the notes on a staff as they are played.

# Challenges
I was attempting to add midi support for use with real keyboards, but I could not get Java's built in midi methods to
recognize the USB midi controller I was able to borrow from Jones. As a fallback measure, I simply used the PC keyboard
which works fine, but is less ergonomic and in most cases does not support ten key rollover, limiting the use of
polyphony. (e.g., my laptop keyboard will only recognize up to 4 keys at a time, and depending on their locations,
often less than that.)

# Lessons learned
Java introduces enough latency that I wouldn't want to use it for a real time software synth.
Swing is pretty clunky, and I wouldn't want to use it for my full project.
Beads is actually a good library, and has useful documentation for people who have never build a synth before
Additive synthesis would quickly get expensive if I wanted to create realistic sounding instruments--this is easier to
achieve with either samples or modular synthesis, most likely FM synthesis. 


