package com.orangex.amazingfellow.network.steam;

import java.util.List;

/**
 * Created by orangex on 2017/11/5.
 */

public class FriendListResultBean {
    
    /**
     * friendslist : {"friends":[{"steamid":"76561197983810397","relationship":"friend","friend_since":1415108817},{"steamid":"76561198057651602","relationship":"friend","friend_since":1412864960},{"steamid":"76561198067909279","relationship":"friend","friend_since":1481803419},{"steamid":"76561198089904089","relationship":"friend","friend_since":1407424565},{"steamid":"76561198091999696","relationship":"friend","friend_since":1407134211},{"steamid":"76561198095983497","relationship":"friend","friend_since":1507917669},{"steamid":"76561198096645968","relationship":"friend","friend_since":1416219412},{"steamid":"76561198096790257","relationship":"friend","friend_since":1427984926},{"steamid":"76561198096954882","relationship":"friend","friend_since":1412147879},{"steamid":"76561198097027139","relationship":"friend","friend_since":1423019538},{"steamid":"76561198097343844","relationship":"friend","friend_since":1413042826},{"steamid":"76561198097359685","relationship":"friend","friend_since":1412175246},{"steamid":"76561198098031771","relationship":"friend","friend_since":1416218719},{"steamid":"76561198098986053","relationship":"friend","friend_since":1471871813},{"steamid":"76561198099091210","relationship":"friend","friend_since":1407134242},{"steamid":"76561198099339833","relationship":"friend","friend_since":1455521002},{"steamid":"76561198099411736","relationship":"friend","friend_since":1407134007},{"steamid":"76561198099514565","relationship":"friend","friend_since":1470971471},{"steamid":"76561198099523346","relationship":"friend","friend_since":1414820677},{"steamid":"76561198099533882","relationship":"friend","friend_since":1413096180},{"steamid":"76561198099832612","relationship":"friend","friend_since":1390629551},{"steamid":"76561198099897885","relationship":"friend","friend_since":1412235136},{"steamid":"76561198100263332","relationship":"friend","friend_since":1503140478},{"steamid":"76561198100560211","relationship":"friend","friend_since":1412214928},{"steamid":"76561198101192921","relationship":"friend","friend_since":1482715615},{"steamid":"76561198102293270","relationship":"friend","friend_since":1424523647},{"steamid":"76561198102407873","relationship":"friend","friend_since":1412297420},{"steamid":"76561198107628774","relationship":"friend","friend_since":1413096103},{"steamid":"76561198109389884","relationship":"friend","friend_since":1406682239},{"steamid":"76561198109562311","relationship":"friend","friend_since":1407305540},{"steamid":"76561198113495035","relationship":"friend","friend_since":1413457376},{"steamid":"76561198114425236","relationship":"friend","friend_since":1415833367},{"steamid":"76561198116008594","relationship":"friend","friend_since":1424247502},{"steamid":"76561198122644491","relationship":"friend","friend_since":1390632037},{"steamid":"76561198122659380","relationship":"friend","friend_since":1413042538},{"steamid":"76561198122821660","relationship":"friend","friend_since":1407419955},{"steamid":"76561198123331303","relationship":"friend","friend_since":1424523435},{"steamid":"76561198123546132","relationship":"friend","friend_since":1494758774},{"steamid":"76561198126414393","relationship":"friend","friend_since":1481892195},{"steamid":"76561198127064878","relationship":"friend","friend_since":1504349497},{"steamid":"76561198131725839","relationship":"friend","friend_since":1413045794},{"steamid":"76561198134418974","relationship":"friend","friend_since":1462426818},{"steamid":"76561198140186836","relationship":"friend","friend_since":1412864484},{"steamid":"76561198141819873","relationship":"friend","friend_since":1407463486},{"steamid":"76561198144055173","relationship":"friend","friend_since":1463117011},{"steamid":"76561198144815695","relationship":"friend","friend_since":1466148386},{"steamid":"76561198146610881","relationship":"friend","friend_since":1408112409},{"steamid":"76561198156536070","relationship":"friend","friend_since":1414493458},{"steamid":"76561198158132869","relationship":"friend","friend_since":1413042547},{"steamid":"76561198158133297","relationship":"friend","friend_since":1446783212},{"steamid":"76561198167379627","relationship":"friend","friend_since":1495758793},{"steamid":"76561198170223266","relationship":"friend","friend_since":1440680828},{"steamid":"76561198177425061","relationship":"friend","friend_since":1507197534},{"steamid":"76561198180443515","relationship":"friend","friend_since":1471161505},{"steamid":"76561198182414234","relationship":"friend","friend_since":1427812083},{"steamid":"76561198188421334","relationship":"friend","friend_since":1502987842},{"steamid":"76561198204278563","relationship":"friend","friend_since":1439301092},{"steamid":"76561198243795286","relationship":"friend","friend_since":1439291804},{"steamid":"76561198259884356","relationship":"friend","friend_since":1466752620},{"steamid":"76561198262423671","relationship":"friend","friend_since":1504015124},{"steamid":"76561198263492666","relationship":"friend","friend_since":1465785755},{"steamid":"76561198263500994","relationship":"friend","friend_since":1475736506},{"steamid":"76561198273323765","relationship":"friend","friend_since":1507904909},{"steamid":"76561198281088288","relationship":"friend","friend_since":1459254782},{"steamid":"76561198297969740","relationship":"friend","friend_since":1491463842},{"steamid":"76561198310344301","relationship":"friend","friend_since":1505644667},{"steamid":"76561198323382415","relationship":"friend","friend_since":1470988207},{"steamid":"76561198333449693","relationship":"friend","friend_since":1503119951},{"steamid":"76561198389452151","relationship":"friend","friend_since":1507369859},{"steamid":"76561198429425280","relationship":"friend","friend_since":1508376054}]}
     */
    
    private FriendslistBean friendslist;
    
    public FriendslistBean getFriendslist() {
        return friendslist;
    }
    
    public void setFriendslist(FriendslistBean friendslist) {
        this.friendslist = friendslist;
    }
    
    public static class FriendslistBean {
        private List<FriendsBean> friends;
        
        public List<FriendsBean> getFriends() {
            return friends;
        }
        
        public void setFriends(List<FriendsBean> friends) {
            this.friends = friends;
        }
        
        public static class FriendsBean {
            /**
             * steamid : 76561197983810397
             * relationship : friend
             * friend_since : 1415108817
             */
            
            private String steamid;
            private String relationship;
            private int friend_since;
            
            public String getSteamid() {
                return steamid;
            }
            
            public void setSteamid(String steamid) {
                this.steamid = steamid;
            }
            
            public String getRelationship() {
                return relationship;
            }
            
            public void setRelationship(String relationship) {
                this.relationship = relationship;
            }
            
            public int getFriend_since() {
                return friend_since;
            }
            
            public void setFriend_since(int friend_since) {
                this.friend_since = friend_since;
            }
        }
    }
}
