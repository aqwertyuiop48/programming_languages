package com.javatpoint;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sun.java.accessibility.util.*;
import com.sun.jdi.*;
import com.sun.management.*;
import com.sun.net.httpserver.*;
import com.sun.nio.sctp.*;
import com.sun.security.auth.*;
import com.sun.security.jgss.*;
import com.sun.source.doctree.*;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.attach.*;
import com.sun.tools.javac.*;
import com.sun.tools.jconsole.*;
import java.applet.*;
import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.net.*;
import java.nio.*;
import java.rmi.*;
import java.security.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.accessibility.*;
import javax.annotation.processing.*;
import javax.crypto.*;
import javax.imageio.*;
import javax.lang.model.*;
import javax.management.*;
import javax.net.*;
import javax.naming.*;
import javax.print.*;
import javax.script.*;
import javax.security.auth.*;
import javax.security.cert.*;
import javax.security.sasl.*;
import javax.smartcardio.*;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.sql.*;
import javax.swing.*;
import javax.tools.*;
import javax.transaction.xa.*;
import javax.xml.*;
import jdk.net.*;
import jdk.nio.*;
import jdk.jfr.*;
import jdk.dynalink.*;
import jdk.jshell.*;
import jdk.javadoc.doclet.*;
import jdk.management.jfr.*;
import jdk.security.jarsigner.*;
import netscape.javascript.*;
import org.ietf.jgss.*;
import org.w3c.dom.*;
import org.xml.sax.*;

@RestController
public class ProductController 
{
    @Autowired
    private IProductService productService;

    private final JdbcTemplate jdbcTemplate;

    public ProductController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------
    // 1️⃣ Get hardcoded products
    // -------------------------
    @GetMapping("/products/hardcoded")
    public List<ProductDTO> getHardcodedProducts() {
        return productService.getHardcodedProducts();
    }

    // -------------------------
    // 2️⃣ Get products from DB
    // -------------------------
    @GetMapping("/products/db")
    public List<ProductDTO> getProductsFromDb() {
        return productService.getProductsFromDb();
    }

    @GetMapping("/products/db1")
    public List<Map<String, Object>> getProductsRaw() {

        getProductsFromDb();
        jdbcTemplate.execute(""" 
Create table If Not Exists Person (id int, email varchar(255));
truncate table person;
insert into Person (id, email) values ('1', 'a@b.com');
insert into Person (id, email) values ('2', 'c@d.com');
insert into Person (id, email) values ('3', 'a@b.com');
                """);
        List<Map<String, Object>> combined = new ArrayList<>();
        for(String i : new ArrayList<>(Arrays.asList(
                "SELECT * FROM products",
                "SELECT * FROM products LIMIT 1",
                "SELECT * FROM products limit 3",
                "select distinct(e.email) from Person e join Person p on e.id != p.id and e.email = p.email;"
        ))) {
            combined.addAll(jdbcTemplate.queryForList(i));
            combined.add(new HashMap<>(Map.of("", "=======================================")));
        }
        return combined;
    }


    // -------------------------
    // 3️⃣ Get product by ID from DB
    // -------------------------
    @GetMapping("/products/db/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    // -------------------------
    // 4️⃣ POST – Create Product in DB
    // -------------------------
    @PostMapping("/products/db")
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }
            /*
            fetch("http://localhost:8080/products/db", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                name: "Laptop",
                batch: "BNV998",
                price: 45000,
                quantity: 2
            })
        }).then(r => r.json()).then(console.log);

             */

    // -------------------------
    // 5️⃣ PUT – Update Product in DB
    // -------------------------
    @PutMapping("/products/db/{id}")
    public ProductDTO updateProduct(@PathVariable int id, @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    /*************************************************** 1  (Leetcode) ********************************************************/

    public int[] twoSum(int[] nums, int target) {
        var hash = new HashMap<Integer, Integer>();
        for(int i=0;i<nums.length;i++){
            if(hash.keySet().contains(target-nums[i]))return new int[]{i,hash.get(target-nums[i])};
            hash.put(nums[i],i);
        }
        return new int[]{0,0};
    }

    @GetMapping("/1")
    public List<int[]> twoSum1() {
        List<int[]> results = new ArrayList<>();
        results.addAll(Arrays.asList(
                twoSum(new int[]{2,7,11,15}, 9),
                twoSum(new int[]{3,2,4}, 6),
                twoSum(new int[]{3,3}, 6)
        ));
        return results;
    }

    /*************************************************  3  **********************************************************/

    public int lengthOfLongestSubstring(String s) {var li = new ArrayList<String>();
        String s1 = "";
        for(int i=0;i<s.length();i++){if(!s1.contains(String.valueOf(s.charAt(i)))) s1 = s1 + s.charAt(i);
        else{li.add(s1);
            s1 = s1.substring( s1.indexOf(s.charAt(i))+1, s1.length()) + s.charAt(i);}}
        li.add(s1);
        return Collections.max(li.stream().map(i->i.length()).collect(Collectors.toList())); }

    @GetMapping("/3")
    public List<Integer> lengthOfLongestSubstring1() {
        List<Integer> results = new ArrayList<>();
        results.add(lengthOfLongestSubstring("abcabcbb"));   // expected 3
        results.add(lengthOfLongestSubstring("bbbbb"));      // expected 1
        results.add(lengthOfLongestSubstring("pwwkew"));     // expected 3
        results.add(lengthOfLongestSubstring("aabaab!bb"));  // expected 3
        return results;
    }
    /*************************************************  5  **********************************************************/

    public String longestPalindrome(String s) {
        return IntStream.range(0, 2 * s.length()).mapToObj(i -> Stream.iterate(new int[]{i / 2, i / 2 + i % 2}, p -> p[0] >= 0 && p[1] < s.length() && s.charAt(p[0]) == s.charAt(p[1]), p -> new int[]{p[0] - 1, p[1] + 1}).reduce((a, b) -> b).orElse(new int[]{i / 2, i / 2 + i % 2 - 1})).max(Comparator.comparingInt(p -> p[1] - p[0])).map(p -> s.substring(p[0], p[1] + 1)).orElse("");
    }

    @GetMapping("/5")
    public List<String> longestPalindrome1() {
        List<String> results = new ArrayList<>();
        results.add(longestPalindrome("abcabcbb"));
        results.add(longestPalindrome("bbbbb"));
        return results;
    }

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/api")
    public void getAPI(HttpServletResponse response) throws IOException {
        String a = "a";
        int b = 1;
        log.info("""
        Step 1: API started , {} , {}.
        """,a,b);

        String msg = String.format("""
        Step 1: API started AGAIN , %s , %d.
        """, a, b);
        response.getWriter().println(msg);
        response.getWriter().println(msg);

    }
    /***********************************************************************************************************/


}
