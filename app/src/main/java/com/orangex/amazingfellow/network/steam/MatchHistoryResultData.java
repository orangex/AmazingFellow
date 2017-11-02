package com.orangex.amazingfellow.network.steam;

import java.util.List;

/**
 * Created by chengyuan.wang on 2017/11/1.
 */

public class MatchHistoryResultData {

    /**
     * result : {"status":1,"num_results":5,"total_results":500,"results_remaining":396,"matches":[{"match_id":3175575332,"match_seq_num":2773652301,"start_time":1494675640,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":229080011,"player_slot":0,"hero_id":67},{"account_id":153229916,"player_slot":1,"hero_id":44},{"account_id":192663106,"player_slot":2,"hero_id":26},{"account_id":140933484,"player_slot":3,"hero_id":71},{"account_id":133886195,"player_slot":4,"hero_id":28},{"account_id":4294967295,"player_slot":128,"hero_id":21},{"account_id":129594894,"player_slot":129,"hero_id":104},{"account_id":133010736,"player_slot":130,"hero_id":84},{"account_id":98371989,"player_slot":131,"hero_id":35},{"account_id":144960018,"player_slot":132,"hero_id":2}]},{"match_id":3175452567,"match_seq_num":2773556716,"start_time":1494672058,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":133552589,"player_slot":0,"hero_id":25},{"account_id":4294967295,"player_slot":1,"hero_id":9},{"account_id":184186379,"player_slot":2,"hero_id":98},{"account_id":319969147,"player_slot":3,"hero_id":42},{"account_id":4294967295,"player_slot":4,"hero_id":88},{"account_id":166025858,"player_slot":128,"hero_id":2},{"account_id":169096366,"player_slot":129,"hero_id":94},{"account_id":137887592,"player_slot":130,"hero_id":14},{"account_id":133886195,"player_slot":131,"hero_id":51},{"account_id":129910452,"player_slot":132,"hero_id":44}]},{"match_id":3175184580,"match_seq_num":2773326781,"start_time":1494663800,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":192500943,"player_slot":0,"hero_id":32},{"account_id":190052711,"player_slot":1,"hero_id":34},{"account_id":126225445,"player_slot":2,"hero_id":70},{"account_id":403402463,"player_slot":3,"hero_id":86},{"account_id":133886195,"player_slot":4,"hero_id":44},{"account_id":140315183,"player_slot":128,"hero_id":114},{"account_id":139716114,"player_slot":129,"hero_id":63},{"account_id":140027962,"player_slot":130,"hero_id":36},{"account_id":207639473,"player_slot":131,"hero_id":21},{"account_id":323451161,"player_slot":132,"hero_id":96}]},{"match_id":3174637634,"match_seq_num":2772821853,"start_time":1494643052,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":251130136,"player_slot":0,"hero_id":106},{"account_id":139648823,"player_slot":1,"hero_id":54},{"account_id":138456667,"player_slot":2,"hero_id":28},{"account_id":186503311,"player_slot":3,"hero_id":112},{"account_id":4294967295,"player_slot":4,"hero_id":33},{"account_id":139482193,"player_slot":128,"hero_id":22},{"account_id":316347661,"player_slot":129,"hero_id":2},{"account_id":403874931,"player_slot":130,"hero_id":96},{"account_id":133886195,"player_slot":131,"hero_id":67},{"account_id":220177787,"player_slot":132,"hero_id":44}]},{"match_id":3174590376,"match_seq_num":2772736574,"start_time":1494640531,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":67},{"account_id":174862923,"player_slot":1,"hero_id":5},{"account_id":138229697,"player_slot":2,"hero_id":101},{"account_id":139397318,"player_slot":3,"hero_id":53},{"account_id":290740796,"player_slot":4,"hero_id":15},{"account_id":145548927,"player_slot":128,"hero_id":4},{"account_id":133886195,"player_slot":129,"hero_id":27},{"account_id":171460111,"player_slot":130,"hero_id":102},{"account_id":220177787,"player_slot":131,"hero_id":95},{"account_id":181554145,"player_slot":132,"hero_id":14}]}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * status : 1
         * num_results : 5
         * total_results : 500
         * results_remaining : 396
         * matches : [{"match_id":3175575332,"match_seq_num":2773652301,"start_time":1494675640,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":229080011,"player_slot":0,"hero_id":67},{"account_id":153229916,"player_slot":1,"hero_id":44},{"account_id":192663106,"player_slot":2,"hero_id":26},{"account_id":140933484,"player_slot":3,"hero_id":71},{"account_id":133886195,"player_slot":4,"hero_id":28},{"account_id":4294967295,"player_slot":128,"hero_id":21},{"account_id":129594894,"player_slot":129,"hero_id":104},{"account_id":133010736,"player_slot":130,"hero_id":84},{"account_id":98371989,"player_slot":131,"hero_id":35},{"account_id":144960018,"player_slot":132,"hero_id":2}]},{"match_id":3175452567,"match_seq_num":2773556716,"start_time":1494672058,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":133552589,"player_slot":0,"hero_id":25},{"account_id":4294967295,"player_slot":1,"hero_id":9},{"account_id":184186379,"player_slot":2,"hero_id":98},{"account_id":319969147,"player_slot":3,"hero_id":42},{"account_id":4294967295,"player_slot":4,"hero_id":88},{"account_id":166025858,"player_slot":128,"hero_id":2},{"account_id":169096366,"player_slot":129,"hero_id":94},{"account_id":137887592,"player_slot":130,"hero_id":14},{"account_id":133886195,"player_slot":131,"hero_id":51},{"account_id":129910452,"player_slot":132,"hero_id":44}]},{"match_id":3175184580,"match_seq_num":2773326781,"start_time":1494663800,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":192500943,"player_slot":0,"hero_id":32},{"account_id":190052711,"player_slot":1,"hero_id":34},{"account_id":126225445,"player_slot":2,"hero_id":70},{"account_id":403402463,"player_slot":3,"hero_id":86},{"account_id":133886195,"player_slot":4,"hero_id":44},{"account_id":140315183,"player_slot":128,"hero_id":114},{"account_id":139716114,"player_slot":129,"hero_id":63},{"account_id":140027962,"player_slot":130,"hero_id":36},{"account_id":207639473,"player_slot":131,"hero_id":21},{"account_id":323451161,"player_slot":132,"hero_id":96}]},{"match_id":3174637634,"match_seq_num":2772821853,"start_time":1494643052,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":251130136,"player_slot":0,"hero_id":106},{"account_id":139648823,"player_slot":1,"hero_id":54},{"account_id":138456667,"player_slot":2,"hero_id":28},{"account_id":186503311,"player_slot":3,"hero_id":112},{"account_id":4294967295,"player_slot":4,"hero_id":33},{"account_id":139482193,"player_slot":128,"hero_id":22},{"account_id":316347661,"player_slot":129,"hero_id":2},{"account_id":403874931,"player_slot":130,"hero_id":96},{"account_id":133886195,"player_slot":131,"hero_id":67},{"account_id":220177787,"player_slot":132,"hero_id":44}]},{"match_id":3174590376,"match_seq_num":2772736574,"start_time":1494640531,"lobby_type":0,"radiant_team_id":0,"dire_team_id":0,"players":[{"account_id":4294967295,"player_slot":0,"hero_id":67},{"account_id":174862923,"player_slot":1,"hero_id":5},{"account_id":138229697,"player_slot":2,"hero_id":101},{"account_id":139397318,"player_slot":3,"hero_id":53},{"account_id":290740796,"player_slot":4,"hero_id":15},{"account_id":145548927,"player_slot":128,"hero_id":4},{"account_id":133886195,"player_slot":129,"hero_id":27},{"account_id":171460111,"player_slot":130,"hero_id":102},{"account_id":220177787,"player_slot":131,"hero_id":95},{"account_id":181554145,"player_slot":132,"hero_id":14}]}]
         */

        private int status;
        private int num_results;
        private int total_results;
        private int results_remaining;
        private List<MatchesBean> matches;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getNum_results() {
            return num_results;
        }

        public void setNum_results(int num_results) {
            this.num_results = num_results;
        }

        public int getTotal_results() {
            return total_results;
        }

        public void setTotal_results(int total_results) {
            this.total_results = total_results;
        }

        public int getResults_remaining() {
            return results_remaining;
        }

        public void setResults_remaining(int results_remaining) {
            this.results_remaining = results_remaining;
        }

        public List<MatchesBean> getMatches() {
            return matches;
        }

        public void setMatches(List<MatchesBean> matches) {
            this.matches = matches;
        }

        public static class MatchesBean {
            /**
             * match_id : 3175575332
             * match_seq_num : 2773652301
             * start_time : 1494675640
             * lobby_type : 0
             * radiant_team_id : 0
             * dire_team_id : 0
             * players : [{"account_id":229080011,"player_slot":0,"hero_id":67},{"account_id":153229916,"player_slot":1,"hero_id":44},{"account_id":192663106,"player_slot":2,"hero_id":26},{"account_id":140933484,"player_slot":3,"hero_id":71},{"account_id":133886195,"player_slot":4,"hero_id":28},{"account_id":4294967295,"player_slot":128,"hero_id":21},{"account_id":129594894,"player_slot":129,"hero_id":104},{"account_id":133010736,"player_slot":130,"hero_id":84},{"account_id":98371989,"player_slot":131,"hero_id":35},{"account_id":144960018,"player_slot":132,"hero_id":2}]
             */

            private long match_id;
            private long match_seq_num;
            private int start_time;
            private int lobby_type;
            private int radiant_team_id;
            private int dire_team_id;
            private List<PlayersBean> players;

            public long getMatch_id() {
                return match_id;
            }

            public void setMatch_id(long match_id) {
                this.match_id = match_id;
            }

            public long getMatch_seq_num() {
                return match_seq_num;
            }

            public void setMatch_seq_num(long match_seq_num) {
                this.match_seq_num = match_seq_num;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getLobby_type() {
                return lobby_type;
            }

            public void setLobby_type(int lobby_type) {
                this.lobby_type = lobby_type;
            }

            public int getRadiant_team_id() {
                return radiant_team_id;
            }

            public void setRadiant_team_id(int radiant_team_id) {
                this.radiant_team_id = radiant_team_id;
            }

            public int getDire_team_id() {
                return dire_team_id;
            }

            public void setDire_team_id(int dire_team_id) {
                this.dire_team_id = dire_team_id;
            }

            public List<PlayersBean> getPlayers() {
                return players;
            }

            public void setPlayers(List<PlayersBean> players) {
                this.players = players;
            }

            public static class PlayersBean {
                /**
                 * account_id : 229080011
                 * player_slot : 0
                 * hero_id : 67
                 */

                private int account_id;
                private int player_slot;
                private int hero_id;

                public int getAccount_id() {
                    return account_id;
                }

                public void setAccount_id(int account_id) {
                    this.account_id = account_id;
                }

                public int getPlayer_slot() {
                    return player_slot;
                }

                public void setPlayer_slot(int player_slot) {
                    this.player_slot = player_slot;
                }

                public int getHero_id() {
                    return hero_id;
                }

                public void setHero_id(int hero_id) {
                    this.hero_id = hero_id;
                }
            }
        }
    }
}
