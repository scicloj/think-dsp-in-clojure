(ns chapter-01
  (:require [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [fastmath.core :as m]
            [fastmath.signal :as signal]
            [scicloj.hanamicloth.v1.plotlycloth
             :as ploclo]
            [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]))


(defn plot-signal [signal time-range]
  (-> {:time time-range}
      tc/dataset      
      (tc/map-columns :amplitude [:time] signal)
      (ploclo/layer-line {:=x :time
                          :=y :amplitude})))

;; # Waveforms
;; ## Generate and plot a sinusoidal waveform
(plot-signal (signal/oscillator :sin 1.0 1.0 0.0)
             (range 0 1 0.001))

;; ## Additive synthesis
;; ### Two sinusoidal waveforms
(let [sig1 (signal/oscillator :sin 1 0.25 0)
      sig2 (signal/oscillator :sin 2 0.75 0)]
  (-> (signal/oscillators-sum sig1 sig2)
      (plot-signal (range 0 3 0.01))))

;; ### Saw and triangle waveforms
(let [sig1 (signal/oscillator :saw 1 0.75 0)
      sig2 (signal/oscillator :triangle 2 0.25 0)]
  (-> (signal/oscillators-sum sig1 sig2)
      (plot-signal (range 0 3 0.01))))

;; # Signals
;; ## Generate a signal from a waveform and save to disk
;; - At sample rate 44.1k, period of 1 second
(kind/hidden (let [sig1 (signal/oscillator :sin 1 0.25 0)
                   sig2 (signal/oscillator :sin 2 0.75 0)]
               (-> (signal/oscillators-sum sig1 sig2)
                   (signal/oscillator->signal 44100 1)
                   (signal/save-signal "output/ch01/signal-01.raw"))))
;; => #object["[D" 0x11c5b926 "[D@11c5b926"]



