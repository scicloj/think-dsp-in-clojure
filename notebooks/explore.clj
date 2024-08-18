(ns explore
  (:require [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.functional :as fun]
            [fastmath.signal :as signal]
            [scicloj.hanamicloth.v1.plotlycloth
             :as ploclo]))

;; ## Tablecloth

;; working with tablecloth columns

(tcc/+ (tcc/column (range 3))
       (tcc/column (repeatedly 3 rand)))

;; ## dtype-next


;; working with dtype-next readers
;; (an array-like abstract notion)


;; lazy and non-caching
(dtype/make-reader :float32 3 (rand))

(-> (dtype/make-reader :float32 3 (rand))
    (fun/* 1000)
    (fun/- 10000))

(-> (dtype/make-reader :float32 3 (rand))
    (fun/* 1000)
    (fun/- 10000))

;; caching now:

(def x
  (-> (dtype/make-reader :float32 3 (rand))
      (fun/* 1000)
      (fun/- 10000)
      dtype/clone))

x

x

;; ## Fastmath

(let [wave-sin (signal/oscillator :sin 1.0 1.0 0.5)]
  (wave-sin 0.1))

;; ## Plotting

(defn plot-signal [signal time-range]
  (-> {:t time-range}
      tc/dataset
      (tc/map-columns :y [:t] signal)
      (ploclo/layer-line {:=x :t
                          :=y :y})))

(plot-signal (signal/oscillator :sin 1.0 1.0 0.5)
             (range -5 5 0.01))

(let [w1 (signal/oscillator :triangle 1.5 0.5 0.5)
      w2 (signal/oscillator :sin 1 0.5 0)]
  (-> (signal/oscillators-sum w1 w2)
      (plot-signal (range -3 3 0.01))))
