package model;
public class Member { 
    private String memberId; 
    private String name; 
    private String email; 
    private String phone; 
    private String membershipDate; 
    public Member() { 
        this.memberId = ""; 
        this.name = ""; 
        this.email = ""; 
        this.phone = ""; 
        this.membershipDate = ""; 
        }
        public Member(String memberId, String name, String email, String phone) { 
            this.memberId = memberId; 
            this.name = name; 
            this.email = email; 
            this.phone = phone; 
            this.membershipDate = java.time.LocalDate.now().toString();  
            public Member(String memberId, String name, String email, String phone, String membershipDate) { 
                this.memberId = memberId; 
                this.name = name; 
                this.email = email; 
                this.phone = phone; 
                this.membershipDate = membershipDate; 
                } 
                public String getMemberId() { return memberId; } 
                public void setMemberId(String memberId) { this.memberId = memberId; } 
                public String getName() { return name; } 
                public void setName(String name) { this.name = name; } 
                public String getEmail() { return email; } 
                public void setEmail(String email) { this.email = email; } 
                public String getPhone() { return phone; } 
                public void setPhone(String phone) { this.phone = phone; } 
                public String getMembershipDate() { return membershipDate; } 
                public void setMembershipDate(String membershipDate) { this.membershipDate = membershipDate; } 
                public String toFileString() { return memberId + "," + name + "," + email + "," + phone + "," + membershipDate; } 
                @Override 
                public String toString() { return "[" + memberId + "] " + name + " (" + email + ", " + phone + ")"; } 
}