var app = function () {
    "use strict";

    function t() {
    }

    function e(t) {
        return t()
    }

    function r() {
        return Object.create(null)
    }

    function n(t) {
        t.forEach(e)
    }

    function a(t) {
        return "function" == typeof t
    }

    function o(t, e) {
        return t != t ? e == e : t !== e || t && "object" == typeof t || "function" == typeof t
    }

    function s(t, e) {
        t.appendChild(e)
    }

    function i(t, e, r) {
        t.insertBefore(e, r || null)
    }

    function l(t) {
        t.parentNode.removeChild(t)
    }

    function c(t, e) {
        for (let r = 0; r < t.length; r += 1) t[r] && t[r].d(e)
    }

    function h(t) {
        return document.createElement(t)
    }

    function u(t) {
        return document.createElementNS("http://www.w3.org/2000/svg", t)
    }

    function d() {
        return t = " ", document.createTextNode(t);
        var t
    }

    function f(t, e, r, n) {
        return t.addEventListener(e, r, n), () => t.removeEventListener(e, r, n)
    }

    function p(t, e, r) {
        null == r ? t.removeAttribute(e) : t.setAttribute(e, r)
    }

    let m;

    function g(t) {
        m = t
    }

    function v(t) {
        (function () {
            if (!m) throw new Error("Function called outside component initialization");
            return m
        })().$$.on_destroy.push(t)
    }

    const $ = [], k = [], w = [], x = [], M = Promise.resolve();
    let y = !1;

    function b(t) {
        w.push(t)
    }

    function q() {
        const t = new Set;
        do {
            for (; $.length;) {
                const t = $.shift();
                g(t), _(t.$$)
            }
            for (; k.length;) k.pop()();
            for (let e = 0; e < w.length; e += 1) {
                const r = w[e];
                t.has(r) || (r(), t.add(r))
            }
            w.length = 0
        } while ($.length);
        for (; x.length;) x.pop()();
        y = !1
    }

    function _(t) {
        t.fragment && (t.update(t.dirty), n(t.before_update), t.fragment.p(t.dirty, t.ctx), t.dirty = null, t.after_update.forEach(b))
    }

    const C = new Set;
    let j;

    function z(t, e) {
        t && t.i && (C.delete(t), t.i(e))
    }

    function E(t, e, r, n) {
        if (t && t.o) {
            if (C.has(t)) return;
            C.add(t), j.c.push(() => {
                C.delete(t), n && (r && t.d(1), n())
            }), t.o(e)
        }
    }

    function B(t, r, o) {
        const {fragment: s, on_mount: i, on_destroy: l, after_update: c} = t.$$;
        s.m(r, o), b(() => {
            const r = i.map(e).filter(a);
            l ? l.push(...r) : n(r), t.$$.on_mount = []
        }), c.forEach(b)
    }

    function L(t, e) {
        t.$$.fragment && (n(t.$$.on_destroy), t.$$.fragment.d(e), t.$$.on_destroy = t.$$.fragment = null, t.$$.ctx = {})
    }

    function S(t, e) {
        t.$$.dirty || ($.push(t), y || (y = !0, M.then(q)), t.$$.dirty = r()), t.$$.dirty[e] = !0
    }

    function I(e, a, o, s, i, l) {
        const c = m;
        g(e);
        const h = a.props || {}, u = e.$$ = {
            fragment: null,
            ctx: null,
            props: l,
            update: t,
            not_equal: i,
            bound: r(),
            on_mount: [],
            on_destroy: [],
            before_update: [],
            after_update: [],
            context: new Map(c ? c.$$.context : []),
            callbacks: r(),
            dirty: null
        };
        let d = !1;
        u.ctx = o ? o(e, h, (t, r, n = r) => (u.ctx && i(u.ctx[t], u.ctx[t] = n) && (u.bound[t] && u.bound[t](n), d && S(e, t)), r)) : h, u.update(), d = !0, n(u.before_update), u.fragment = s(u.ctx), a.target && (a.hydrate ? u.fragment.l(function (t) {
            return Array.from(t.childNodes)
        }(a.target)) : u.fragment.c(), a.intro && z(e.$$.fragment), B(e, a.target, a.anchor), q()), g(c)
    }

    class N {
        $destroy() {
            L(this, 1), this.$destroy = t
        }

        $on(t, e) {
            const r = this.$$.callbacks[t] || (this.$$.callbacks[t] = []);
            return r.push(e), () => {
                const t = r.indexOf(e);
                -1 !== t && r.splice(t, 1)
            }
        }

        $set() {
        }
    }

    function O(t) {
        return t >= 10 ? t.toString() : `0${t}`
    }

    function T(t) {
        return t.padStart(9, " ")
    }

    function A(t) {
        var e, r, n, a, o, c, h, d, f, m, g, v;
        return {
            c() {
                e = u("g"), r = u("use"), a = u("g"), o = u("use"), h = u("g"), d = u("use"), m = u("g"), g = u("use"), p(r, "href", n = "#" + t.hours[0]), p(e, "transform", "translate(14 0)"), p(o, "href", c = "#" + t.hours[1]), p(a, "transform", "translate(35 0)"), p(d, "href", f = "#" + t.minutes[0]), p(h, "transform", "translate(65 0)"), p(g, "href", v = "#" + t.minutes[1]), p(m, "transform", "translate(86 0)")
            }, m(t, n) {
                i(t, e, n), s(e, r), i(t, a, n), s(a, o), i(t, h, n), s(h, d), i(t, m, n), s(m, g)
            }, p(t, e) {
                t.hours && n !== (n = "#" + e.hours[0]) && p(r, "href", n), t.hours && c !== (c = "#" + e.hours[1]) && p(o, "href", c), t.minutes && f !== (f = "#" + e.minutes[0]) && p(d, "href", f), t.minutes && v !== (v = "#" + e.minutes[1]) && p(g, "href", v)
            }, d(t) {
                t && (l(e), l(a), l(h), l(m))
            }
        }
    }

    function D(e) {
        var r, n, a, o, c, h, d, f, m, g, v, $, k, w, x, M, y, b, q, _, C, j, z = e.now && A(e);
        return {
            c() {
                r = u("svg"), n = u("defs"), a = u("path"), o = u("path"), c = u("path"), h = u("path"), d = u("path"), f = u("g"), m = u("use"), g = u("path"), v = u("path"), $ = u("path"), k = u("path"), w = u("rect"), x = u("g"), M = u("g"), z && z.c(), y = u("g"), b = u("use"), q = u("use"), _ = u("g"), C = u("g"), j = u("path"), p(a, "id", "0"), p(a, "d", "M -6 -15 h 12 v 30 h -12 z"), p(o, "id", "1"), p(o, "d", "M 6 -15 v 30"), p(c, "id", "2"), p(c, "d", "M -6 -15 h 12 v 15 h -12 v 15 h 12"), p(h, "id", "3"), p(h, "d", "M -6 -15 h 12 v 30 h -12 m 0 -15 h 12"), p(d, "id", "4"), p(d, "d", "M -6 -15 v 15 h 12 v 15 m 0 -30 v 15"), p(m, "href", "#2"), p(m, "transform", "scale(-1 1)"), p(f, "id", "5"), p(g, "id", "6"), p(g, "d", "M 6 -15 h -12 v 30 h 12 v -15 h -12"), p(v, "id", "7"), p(v, "d", "M -6 -15 h 12 v 30"), p($, "id", "8"), p($, "d", "M -6 -15 h 12 v 30 h -12 z m 0 15 h 12"), p(k, "id", "9"), p(k, "d", "M 6 15 v -30 h -12 v 15 h 12"), p(w, "id", "square"), p(w, "x", "-2.5"), p(w, "y", "-2.5"), p(w, "width", "5"), p(w, "height", "5"), p(M, "fill", "none"), p(M, "stroke", "currentColor"), p(M, "stroke-width", "5"), p(M, "stroke-linecap", "square"), p(M, "stroke-linejoin", "square"), p(b, "href", "#square"), p(b, "y", "-10"), p(q, "href", "#square"), p(q, "y", "10"), p(y, "class", "colon svelte-xfw2u4"), p(y, "transform", "translate(50 0)"), p(x, "transform", "translate(0 50)"), p(j, "d", "M 0 0 q -25 -5 -25 -25 q -10 0 -10 12 q 0 -20 5 -20 q -15 0 -15 10 a 22.5 22.5 0 0 1 45 0 q 10 0 15 -20 q 0 -8 -5 -8 q 15 0 15 8 l 5 5 l -5 5 q 0 33 -25 33"), p(C, "opacity", "0.35"), p(C, "fill", "currentColor"), p(C, "stroke", "currentColor"), p(C, "stroke-width", "5"), p(C, "stroke-linecap", "round"), p(C, "stroke-linejoin", "round"), p(_, "transform", "translate(25 100) scale(0.45)"), p(r, "viewBox", "0 0 100 100"), p(r, "class", "svelte-xfw2u4")
            }, m(t, e) {
                i(t, r, e), s(r, n), s(n, a), s(n, o), s(n, c), s(n, h), s(n, d), s(n, f), s(f, m), s(n, g), s(n, v), s(n, $), s(n, k), s(n, w), s(r, x), s(x, M), z && z.m(M, null), s(x, y), s(y, b), s(y, q), s(r, _), s(_, C), s(C, j)
            }, p(t, e) {
                e.now ? z ? z.p(t, e) : ((z = A(e)).c(), z.m(M, null)) : z && (z.d(1), z = null)
            }, i: t, o: t, d(t) {
                t && l(r), z && z.d()
            }
        }
    }

    function P(t, e, r) {
        let n, a, o = new Date, s = setInterval(() => {
            r("now", o = new Date)
        }, 1e3);
        return v(() => {
            clearInterval(s)
        }), t.$$.update = (t = {now: 1}) => {
            t.now && r("hours", n = O(o.getHours())), t.now && r("minutes", a = O(o.getMinutes()))
        }, {now: o, hours: n, minutes: a}
    }

    class H extends N {
        constructor(t) {
            super(), I(this, t, P, D, o, [])
        }
    }

    function F(t, e, r) {
        const n = Object.create(t);
        return n.href = e[r].href, n.area = e[r].area, n
    }

    function G(t, e, r) {
        const n = Object.create(t);
        return n.character = e[r], n.index = r, n
    }

    function J(e) {
        var r;
        return {
            c() {
                p(r = u("use"), "href", "#asterisk")
            }, m(t, e) {
                i(t, r, e)
            }, p: t, d(t) {
                t && l(r)
            }
        }
    }

    function K(t) {
        var e, r;
        return {
            c() {
                p(e = u("use"), "href", r = "#" + t.operator)
            }, m(t, r) {
                i(t, e, r)
            }, p(t, n) {
                t.operator && r !== (r = "#" + n.operator) && p(e, "href", r)
            }, d(t) {
                t && l(e)
            }
        }
    }

    function Q(e) {
        var r;
        return {
            c() {
                p(r = u("use"), "href", "#placeholder")
            }, m(t, e) {
                i(t, r, e)
            }, p: t, d(t) {
                t && l(r)
            }
        }
    }

    function R(t) {
        var e, r;
        return {
            c() {
                p(e = u("use"), "href", r = "#" + t.character)
            }, m(t, r) {
                i(t, e, r)
            }, p(t, n) {
                t.display && r !== (r = "#" + n.character) && p(e, "href", r)
            }, d(t) {
                t && l(e)
            }
        }
    }

    function U(t) {
        var e;

        function r(t, e) {
            return " " !== e.character ? R : Q
        }

        var n = r(0, t), a = n(t);
        return {
            c() {
                e = u("g"), a.c(), p(e, "transform", "translate(" + (61 + 39 * t.index) + " 0)")
            }, m(t, r) {
                i(t, e, r), a.m(e, null)
            }, p(t, o) {
                n === (n = r(0, o)) && a ? a.p(t, o) : (a.d(1), (a = n(o)) && (a.c(), a.m(e, null)))
            }, d(t) {
                t && l(e), a.d()
            }
        }
    }

    function V(t) {
        var e, r, n, a, o, c;

        function m() {
            return t.click_handler(t)
        }

        return {
            c() {
                var s, i, l, g;
                e = h("button"), r = u("svg"), n = u("g"), a = u("use"), o = d(), p(a, "href", "#" + t.href), p(n, "fill", "none"), p(n, "stroke", "currentColor"), p(n, "stroke-width", "6"), p(n, "stroke-linecap", "square"), p(n, "stroke-linejoin", "square"), p(r, "viewBox", "0 0 40 40"), p(r, "width", "40"), p(r, "height", "40"), p(r, "class", "svelte-1knese7"), p(e, "aria-label", t.area), s = e, i = "grid-area", l = t.area, s.style.setProperty(i, l, g ? "important" : ""), p(e, "class", "svelte-1knese7"), c = f(e, "click", m)
            }, m(t, l) {
                i(t, e, l), s(e, r), s(r, n), s(n, a), s(e, o)
            }, p(e, r) {
                t = r
            }, d(t) {
                t && l(e), c()
            }
        }
    }

    function W(e) {
        var r, n, a, o, f, m, g, v, $, k, w, x, M, y, b, q, _, C, j, z, E, B, L, S, I, N, O, A, D;

        function P(t, e) {
            return e.operator ? K : J
        }

        var H = P(0, e), Q = H(e);
        let R = T(e.display), W = [];
        for (let t = 0; t < R.length; t += 1) W[t] = U(G(e, R, t));
        let X = e.buttons, Y = [];
        for (let t = 0; t < X.length; t += 1) Y[t] = V(F(e, X, t));
        return {
            c() {
                r = u("svg"), n = u("defs"), a = u("path"), o = u("path"), f = u("path"), m = u("path"), g = u("path"), v = u("path"), $ = u("path"), k = u("path"), w = u("path"), x = u("path"), M = u("path"), y = u("path"), b = u("path"), q = u("path"), _ = u("path"), C = u("path"), j = u("path"), z = u("path"), E = u("path"), B = u("use"), L = u("g"), S = u("rect"), I = u("g"), N = u("g"), O = u("g"), Q.c();
                for (let t = 0; t < W.length; t += 1) W[t].c();
                A = d(), D = h("section");
                for (let t = 0; t < Y.length; t += 1) Y[t].c();
                p(a, "id", "0"), p(a, "d", "M 10 5 h 20 v 30 h -20 z"), p(o, "id", "1"), p(o, "d", "M 20 5 h 5 v 30 m -5 0 h 10"), p(f, "id", "2"), p(f, "d", "M 10 5 h 20 v 15 h -20 v 15 h 20"), p(m, "id", "3"), p(m, "d", "M 10 5 h 20 v 30 h -20 m 0 -15 h 20"), p(g, "id", "4"), p(g, "d", "M 10 5 v 15 h 20 m -5 -15 v 30"), p(v, "id", "5"), p(v, "d", "M 30 5 h -20 v 15 h 20 v 15 h -20"), p($, "id", "6"), p($, "d", "M 30 5 h -20 v 30 h 20 v -15 h -20"), p(k, "id", "7"), p(k, "d", "M 10 5 h 20 v 30"), p(w, "id", "8"), p(w, "d", "M 10 5 h 20 v 30 h -20 z m 0 15 h 20"), p(x, "id", "9"), p(x, "d", "M 30 35 v -30 h -20 v 15 h 20"), p(M, "id", "+"), p(M, "d", "M 5 20 h 30 m -15 -15 v 30"), p(y, "id", "-"), p(y, "d", "M 5 20 h 30"), p(b, "id", "*"), p(b, "d", "M 7 7 l 26 26 m 0 -26 l -26 26"), p(q, "id", "/"), p(q, "d", "M 5 20 h 30 m -15 -15 v 0 m 0 30 v 0"), p(_, "id", "="), p(_, "d", "M 5 12 h 30 m -30 16 h 30"), p(C, "id", "."), p(C, "d", "M 10 35 h 0"), p(j, "id", "clear"), p(j, "d", "M 32 12 v -7 h -24 v 30 h 24 v -7"), p(z, "id", "asterisk"), p(z, "d", "M 7 7 l 26 26 m 0 -26 l -26 26 m 13 1 v -28 m -14 14 h 28"), p(E, "id", "?"), p(E, "d", "M 8 15 v -10 h 24 v 15 h -12 v 5 m 0 10 v 0"), p(B, "id", "placeholder"), p(B, "opacity", "0.35"), p(B, "href", "#8"), p(S, "x", "8"), p(S, "y", "-5"), p(S, "width", "434"), p(S, "height", "90"), p(L, "fill", "none"), p(L, "stroke", "currentColor"), p(L, "stroke-width", "16"), p(O, "transform", "translate(10 0)"), p(N, "fill", "none"), p(N, "stroke", "currentColor"), p(N, "stroke-width", "6"), p(N, "stroke-linecap", "square"), p(N, "stroke-linejoin", "square"), p(I, "transform", "translate(15 20)"), p(r, "viewBox", "0 0 450 80"), p(r, "width", "450"), p(r, "height", "80"), p(r, "class", "svelte-1knese7"), p(D, "class", "svelte-1knese7")
            }, m(t, e) {
                i(t, r, e), s(r, n), s(n, a), s(n, o), s(n, f), s(n, m), s(n, g), s(n, v), s(n, $), s(n, k), s(n, w), s(n, x), s(n, M), s(n, y), s(n, b), s(n, q), s(n, _), s(n, C), s(n, j), s(n, z), s(n, E), s(n, B), s(r, L), s(L, S), s(r, I), s(I, N), s(N, O), Q.m(O, null);
                for (let t = 0; t < W.length; t += 1) W[t].m(N, null);
                i(t, A, e), i(t, D, e);
                for (let t = 0; t < Y.length; t += 1) Y[t].m(D, null)
            }, p(t, e) {
                if (H === (H = P(0, e)) && Q ? Q.p(t, e) : (Q.d(1), (Q = H(e)) && (Q.c(), Q.m(O, null))), t.ninePadded || t.display) {
                    let r;
                    for (R = T(e.display), r = 0; r < R.length; r += 1) {
                        const n = G(e, R, r);
                        W[r] ? W[r].p(t, n) : (W[r] = U(n), W[r].c(), W[r].m(N, null))
                    }
                    for (; r < W.length; r += 1) W[r].d(1);
                    W.length = R.length
                }
                if (t.buttons) {
                    let r;
                    for (X = e.buttons, r = 0; r < X.length; r += 1) {
                        const n = F(e, X, r);
                        Y[r] ? Y[r].p(t, n) : (Y[r] = V(n), Y[r].c(), Y[r].m(D, null))
                    }
                    for (; r < Y.length; r += 1) Y[r].d(1);
                    Y.length = X.length
                }
            }, i: t, o: t, d(t) {
                t && l(r), Q.d(), c(W, t), t && (l(A), l(D)), c(Y, t)
            }
        }
    }

    const X = /\d/, Y = /[+\-*/]/;

    function Z(t, e, r) {
        let n, a = 0, o = 0, s = 0, i = "";

        function l(t) {
            s && (o = s, s = 0, r("display", i = "")), r("display", i = i.length < 9 ? `${i}${t}` : i), a = parseFloat(i)
        }

        function c() {
            r("display", i = "?".repeat(9)), r("operator", n = null), a = 0, o = 0, s = 0
        }

        function h() {
            if ("/" === n && 0 === a) c(); else {
                switch (n) {
                    case"+":
                        s = o + a;
                        break;
                    case"-":
                        s = o - a;
                        break;
                    case"*":
                        s = o * a;
                        break;
                    case"/":
                        s = o / a;
                        break;
                    default:
                        s = a
                }
                s > 999999999 || s < -99999999 ? c() : (r("operator", n = null), a = 0, o = s, r("display", i = s.toString()))
            }
        }

        function u(t) {
            if (X.test(t)) l(t); else if (Y.test(t)) e = t, n ? (h(), r("operator", n = e)) : (o = a, a = 0, r("operator", n = e), r("display", i = "")); else switch (t) {
                case".":
                    i.includes(".") || l(i ? "." : "0.");
                    break;
                case"=":
                    h();
                    break;
                case"clear":
                    r("display", i = ""), r("operator", n = null), a = 0, o = 0, s = 0
            }
            var e
        }

        return {
            operator: n,
            display: i,
            handleClick: u,
            buttons: [{href: 0, area: "zero"}, {href: 1, area: "one"}, {href: 2, area: "two"}, {
                href: 3,
                area: "three"
            }, {href: 4, area: "four"}, {href: 5, area: "five"}, {href: 6, area: "six"}, {
                href: 7,
                area: "seven"
            }, {href: 8, area: "eight"}, {href: 9, area: "nine"}, {href: ".", area: "decimal"}, {
                href: "+",
                area: "addition"
            }, {href: "-", area: "subtraction"}, {href: "*", area: "multiplication"}, {
                href: "/",
                area: "division"
            }, {href: "=", area: "equal"}, {href: "clear", area: "clear"}],
            click_handler: ({href: t}) => u(t)
        }
    }

    class tt extends N {
        constructor(t) {
            super(), I(this, t, Z, W, o, [])
        }
    }

    function et(t, e, r) {
        const n = Object.create(t);
        return n.letter = e[r], n.index = r, n
    }

    function rt(t) {
        var e, r, n, a;
        return {
            c() {
                e = u("g"), p(r = u("use"), "href", n = "#" + t.letter), p(e, "transform", a = "translate(" + (68 - 12 * (t.digits.length - 1) + 12 * t.index) + " 0) scale(0.5)")
            }, m(t, n) {
                i(t, e, n), s(e, r)
            }, p(t, o) {
                t.digits && n !== (n = "#" + o.letter) && p(r, "href", n), t.digits && a !== (a = "translate(" + (68 - 12 * (o.digits.length - 1) + 12 * o.index) + " 0) scale(0.5)") && p(e, "transform", a)
            }, d(t) {
                t && l(e)
            }
        }
    }

    function nt(e) {
        var r, n, a, o, m, g, v, $, k, w, x, M, y, b, q, _, C, j, z, E, B, L;
        let S = e.digits, I = [];
        for (let t = 0; t < S.length; t += 1) I[t] = rt(et(e, S, t));
        return {
            c() {
                r = u("svg"), n = u("defs"), a = u("path"), o = u("path"), m = u("path"), g = u("path"), v = u("path"), $ = u("g"), k = u("use"), w = u("path"), x = u("path"), M = u("path"), y = u("path"), b = u("path"), q = u("g"), _ = u("g"), C = u("g"), j = u("path"), z = u("g");
                for (let t = 0; t < I.length; t += 1) I[t].c();
                E = d(), (B = h("button")).innerHTML = '<svg viewBox="0 0 100 100" width="35" height="35" class="svelte-q3c1vs"><g transform="translate(50 50)"><g fill="none" stroke="currentColor" stroke-width="20"><use href="#add"></use></g></g></svg>', p(a, "id", "0"), p(a, "d", "M -6 -15 h 12 v 30 h -12 z"), p(o, "id", "1"), p(o, "d", "M -2 -15 h 5 v 30 m -5 0 h 8"), p(m, "id", "2"), p(m, "d", "M -6 -15 h 12 v 15 h -12 v 15 h 12"), p(g, "id", "3"), p(g, "d", "M -6 -15 h 12 v 30 h -12 m 0 -15 h 12"), p(v, "id", "4"), p(v, "d", "M -6 -15 v 15 h 12 m -3 -15 v 30"), p(k, "href", "#2"), p(k, "transform", "scale(-1 1)"), p($, "id", "5"), p(w, "id", "6"), p(w, "d", "M 6 -15 h -12 v 30 h 12 v -15 h -12"), p(x, "id", "7"), p(x, "d", "M -6 -15 h 12 v 30"), p(M, "id", "8"), p(M, "d", "M -6 -15 h 12 v 30 h -12 z m 0 15 h 12"), p(y, "id", "9"), p(y, "d", "M 6 15 v -30 h -12 v 15 h 12"), p(b, "id", "add"), p(b, "d", "M -40 0 h 80 m -40 -40 v 80"), p(j, "d", "M 27 -14 h 46 l 5 5 v 18 l -5 5 h -46 l -5 -5 v -18 z"), p(C, "stroke-width", "3"), p(C, "opacity", "0.5"), p(z, "stroke-width", "5"), p(_, "fill", "none"), p(_, "stroke", "currentColor"), p(_, "stroke-linecap", "square"), p(_, "stroke-linejoin", "square"), p(q, "transform", "translate(0 30)"), p(r, "viewBox", "0 0 100 60"), p(r, "class", "svelte-q3c1vs"), p(B, "aria-label", "Increment counter"), p(B, "class", "svelte-q3c1vs"), L = f(B, "click", e.click_handler)
            }, m(t, e) {
                i(t, r, e), s(r, n), s(n, a), s(n, o), s(n, m), s(n, g), s(n, v), s(n, $), s($, k), s(n, w), s(n, x), s(n, M), s(n, y), s(n, b), s(r, q), s(q, _), s(_, C), s(C, j), s(_, z);
                for (let t = 0; t < I.length; t += 1) I[t].m(z, null);
                i(t, E, e), i(t, B, e)
            }, p(t, e) {
                if (t.digits) {
                    let r;
                    for (S = e.digits, r = 0; r < S.length; r += 1) {
                        const n = et(e, S, r);
                        I[r] ? I[r].p(t, n) : (I[r] = rt(n), I[r].c(), I[r].m(z, null))
                    }
                    for (; r < I.length; r += 1) I[r].d(1);
                    I.length = S.length
                }
            }, i: t, o: t, d(t) {
                t && l(r), c(I, t), t && (l(E), l(B)), L()
            }
        }
    }

    function at(t, e, r) {
        let n = 0;
        let a;
        return t.$$.update = (t = {count: 1}) => {
            t.count && r("digits", a = n.toString())
        }, {count: n, digits: a, click_handler: () => r("count", n = Math.min(n + 1, 9999))}
    }

    class ot extends N {
        constructor(t) {
            super(), I(this, t, at, nt, o, [])
        }
    }

    function st(e) {
        var r, n, a, o, c, h, d, f, m, g, v, $, k, w, x, M, y, b, q, _, C, j;
        return {
            c() {
                r = u("svg"), n = u("defs"), a = u("rect"), o = u("g"), c = u("g"), h = u("g"), d = u("use"), f = u("use"), m = u("use"), g = u("g"), v = u("use"), $ = u("use"), k = u("use"), w = u("use"), x = u("use"), M = u("g"), y = u("g"), b = u("path"), _ = u("g"), C = u("path"), p(a, "id", "square"), p(a, "x", "-2.5"), p(a, "y", "-2.5"), p(a, "width", "5"), p(a, "height", "5"), p(d, "transform", "rotate(0) translate(0 -40)"), p(d, "href", "#square"), p(f, "transform", "rotate(90) translate(0 -40)"), p(f, "href", "#square"), p(h, "id", "dial-main"), p(m, "href", "#dial-main"), p(m, "transform", "scale(-1 -1)"), p(v, "transform", "rotate(30) translate(0 -40) rotate(-30)"), p(v, "href", "#square"), p($, "transform", "rotate(60) translate(0 -40) rotate(-60)"), p($, "href", "#square"), p(k, "transform", "rotate(120) translate(0 -40) rotate(-120)"), p(k, "href", "#square"), p(w, "transform", "rotate(150) translate(0 -40) rotate(-150)"), p(w, "href", "#square"), p(g, "id", "dial-support"), p(g, "opacity", "0.5"), p(x, "href", "#dial-support"), p(x, "transform", "scale(-1 1)"), p(c, "fill", "currentColor"), p(c, "stroke", "none"), p(b, "opacity", "0.5"), p(b, "d", "M 0 0 v -15"), p(b, "stroke-width", "4"), p(y, "transform", q = "rotate(" + 30 * e.hours + ")"), p(C, "d", "M 0 0 v -30"), p(C, "stroke-width", "2.5"), p(_, "transform", j = "rotate(" + 6 * e.minutes + ")"), p(M, "fill", "none"), p(M, "stroke", "currentColor"), p(M, "stroke-linecap", "square"), p(M, "stroke-width", "4"), p(o, "transform", "translate(50 50)"), p(r, "viewBox", "0 0 100 100"), p(r, "class", "svelte-5bte90")
            }, m(t, e) {
                i(t, r, e), s(r, n), s(n, a), s(r, o), s(o, c), s(c, h), s(h, d), s(h, f), s(c, m), s(c, g), s(g, v), s(g, $), s(g, k), s(g, w), s(c, x), s(o, M), s(M, y), s(y, b), s(M, _), s(_, C)
            }, p(t, e) {
                t.hours && q !== (q = "rotate(" + 30 * e.hours + ")") && p(y, "transform", q), t.minutes && j !== (j = "rotate(" + 6 * e.minutes + ")") && p(_, "transform", j)
            }, i: t, o: t, d(t) {
                t && l(r)
            }
        }
    }

    function it(t, e, r) {
        let n, a, o = new Date, s = setInterval(() => {
            r("now", o = new Date)
        }, 1e3);
        return v(() => {
            clearInterval(s)
        }), t.$$.update = (t = {now: 1}) => {
            t.now && r("hours", n = o.getHours()), t.now && r("minutes", a = o.getMinutes())
        }, {hours: n, minutes: a}
    }

    class lt extends N {
        constructor(t) {
            super(), I(this, t, it, st, o, [])
        }
    }

    function ct(t) {
        var e, r = new lt({});
        return {
            c() {
                r.$$.fragment.c()
            }, m(t, n) {
                B(r, t, n), e = !0
            }, i(t) {
                e || (z(r.$$.fragment, t), e = !0)
            }, o(t) {
                E(r.$$.fragment, t), e = !1
            }, d(t) {
                L(r, t)
            }
        }
    }

    function ht(t) {
        var e, r = new ot({});
        return {
            c() {
                r.$$.fragment.c()
            }, m(t, n) {
                B(r, t, n), e = !0
            }, i(t) {
                e || (z(r.$$.fragment, t), e = !0)
            }, o(t) {
                E(r.$$.fragment, t), e = !1
            }, d(t) {
                L(r, t)
            }
        }
    }

    function ut(t) {
        var e, r = new tt({});
        return {
            c() {
                r.$$.fragment.c()
            }, m(t, n) {
                B(r, t, n), e = !0
            }, i(t) {
                e || (z(r.$$.fragment, t), e = !0)
            }, o(t) {
                E(r.$$.fragment, t), e = !1
            }, d(t) {
                L(r, t)
            }
        }
    }

    function dt(t) {
        var e, r = new H({});
        return {
            c() {
                r.$$.fragment.c()
            }, m(t, n) {
                B(r, t, n), e = !0
            }, i(t) {
                e || (z(r.$$.fragment, t), e = !0)
            }, o(t) {
                E(r.$$.fragment, t), e = !1
            }, d(t) {
                L(r, t)
            }
        }
    }

    function ft(t) {
        var e, r, a, o, c, u, m, g, v, $, k, w, x = [dt, ut, ht, ct], M = [];

        function y(t, e) {
            return 0 === e.app ? 0 : 1 === e.app ? 1 : 2 === e.app ? 2 : 3
        }

        return o = y(0, t), c = M[o] = x[o](t), {
            c() {
                e = h("div"), r = h("section"), a = h("main"), c.c(), u = d(), m = h("nav"), g = h("button"), v = d(), $ = h("button"), p(a, "class", "changing svelte-1x9sc7x"), p(r, "class", "svelte-1x9sc7x"), p(g, "aria-label", "Previous Gadget"), p(g, "class", "svelte-1x9sc7x"), p($, "arial-label", "Next Gadget"), p($, "class", "svelte-1x9sc7x"), p(m, "class", "svelte-1x9sc7x"), p(e, "class", "svelte-1x9sc7x"), w = [f(g, "click", t.click_handler), f($, "click", t.click_handler_1)]
            }, m(n, l) {
                i(n, e, l), s(e, r), s(r, a), M[o].m(a, null), t.main_binding(a), s(e, u), s(e, m), s(m, g), s(m, v), s(m, $), k = !0
            }, p(t, e) {
                var r = o;
                (o = y(0, e)) !== r && (j = {r: 0, c: [], p: j}, E(M[r], 1, 1, () => {
                    M[r] = null
                }), j.r || n(j.c), j = j.p, (c = M[o]) || (c = M[o] = x[o](e)).c(), z(c, 1), c.m(a, null))
            }, i(t) {
                k || (z(c), k = !0)
            }, o(t) {
                E(c), k = !1
            }, d(r) {
                r && l(e), M[o].d(), t.main_binding(null), n(w)
            }
        }
    }

    function pt(t, e, r) {
        let n, a, o = 0;

        function s(t) {
            n.classList.contains("changing") || (n.classList.add("changing"), a = setTimeout(() => {
                r("app", o = "next" === t ? 3 === o ? 0 : o + 1 : 0 === o ? 3 : o - 1), n.classList.remove("changing"), clearTimeout(a)
            }, 800))
        }

        v(() => {
            clearTimeout(a)
        });
        return t.$$.update = (t = {screen: 1}) => {
            t.screen && n && n.classList.remove("changing")
        }, {
            app: o, screen: n, changeApp: s, main_binding: function (t) {
                k[t ? "unshift" : "push"](() => {
                    r("screen", n = t)
                })
            }, click_handler: () => s("prev"), click_handler_1: () => s("next")
        }
    }

    return new class extends N {
        constructor(t) {
            super(), I(this, t, pt, ft, o, [])
        }
    }({target: document.getElementById("timeDateDiv")})
}();